package co.nuqui.tech.msdeposits.domain.service;

import co.nuqui.tech.msdeposits.app.config.KafkaProducer;
import co.nuqui.tech.msdeposits.domain.dto.Deposit;
import co.nuqui.tech.msdeposits.domain.dto.Transaction;
import co.nuqui.tech.msdeposits.domain.dto.User;
import co.nuqui.tech.msdeposits.infrastructure.controller.GlobalException;
import co.nuqui.tech.msdeposits.infrastructure.persistance.DepositRepostory;
import co.nuqui.tech.msdeposits.infrastructure.persistance.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static co.nuqui.tech.msdeposits.domain.dto.DepositConstants.DEFAULT_NAME;
import static co.nuqui.tech.msdeposits.domain.dto.TransferConstants.*;

@SuppressWarnings("BlockingMethodInNonBlockingContext")
@Service
public class DepositService {
    private static final Logger logger = LoggerFactory.getLogger(DepositService.class);

    @Autowired
    private DepositRepostory depositRepostory;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${nuqui.tech.transfer.fixed.fee}")
    private BigDecimal fixedFee;

    @Value("${nuqui.tech.deposit.id.fixed.fee}")
    private UUID nuquiTechDepositIdFixedFee;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${nuqui.tech.kafka.deposits.search.topic}")
    private String depositsSearchTopic;

    @Value("${nuqui.tech.kafka.deposits.transactions.topic}")
    private String depositsTransactionsTopic;

    @Value("${nuqui.tech.kafka.deposits.transaction.file.created.topic}")
    private String generateTransactionsFileByChronTopic;

    @Value("${nuqui.tech.kafka.deposits.transfer.topic}")
    private String depositsTransfersTopic;

    @Value("${nuqui.tech.kafka.deposits.transaction.file.location}")
    private String fileLocation;

    public Mono<Deposit> save(Mono<Deposit> deposit) {
        return deposit.flatMap(depositRepostory::save);
    }

    public Flux<Transaction> findAllTransactionsByAccountFrom(UUID depositIdFrom, int limit, int offset) {
        return transactionRepository
                .findAllTransactionsByDepositIdFrom(depositIdFrom, limit, offset)
                .doOnNext(transaction -> kafkaProducer.send(depositsTransactionsTopic, transaction));
    }

    public Mono<Deposit> createDepositForHuman(User user) {

        Deposit deposit = Deposit.builder()
                .humanId(String.valueOf(user.getHumanId()))
                .userId(user.getId())
                .name(DEFAULT_NAME)
                .balance("0")
                .build();

        return Mono.just(deposit)
                .flatMap(depositRepostory::save);
    }

    public List<Deposit> findByHumanIdOrUserId(Long humanId, String userId) {
        if (humanId == null && (userId == null || userId.isEmpty()))
            throw new GlobalException("Either human-id or user-id is required");
        if (humanId != null && userId != null)
            throw new GlobalException("human-id and user-id are not allowed at the same time");

        if (humanId != null) {
            logger.info("find deposits by humanId: {}", humanId);
            return depositRepostory.findByHumanId(humanId).collectList()
                    .doOnNext(transaction -> kafkaProducer.send(depositsSearchTopic, transaction)).block();
        }

        logger.info("find deposits by userId: {}", userId);
        return depositRepostory
                .findByUserId(userId).collectList()
                .doOnNext(transaction -> kafkaProducer.send(depositsSearchTopic, transaction)).block();

    }

    public Mono<Transaction> transfer(Transaction transaction) {
        transaction.setStatus(TRANSACTION_STATUS_PROCESSING);

        return transactionRepository.save(transaction)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(tuple -> {
                    Deposit sourceAccount = depositRepostory.findById(transaction.getDepositIdFrom()).block();
                    Deposit destinationAccount = depositRepostory.findById(transaction.getDepositIdTo()).block();

                    assert sourceAccount != null;
                    BigDecimal sourceBalance = new BigDecimal(sourceAccount.getBalance());
                    assert destinationAccount != null;
                    BigDecimal destinationBalance = new BigDecimal(destinationAccount.getBalance());

                    transaction.setFee(fixedFee);
                    transaction.setFeeDepositIdTo(nuquiTechDepositIdFixedFee);
                    transaction.setTotalTransactionAmount(transaction.getAmount().add(fixedFee));

                    if (sourceBalance.compareTo(transaction.getTotalTransactionAmount()) < 0) {
                        transaction.setStatus(TRANSACTION_STATUS_INSUFICIENT_FUNDS);
                        return transactionRepository.save(transaction)
                                .flatMap(savedTransaction -> Mono.<Transaction>error(new GlobalException("Insufficient funds")));
                    }

                    BigDecimal newSourceBalance = sourceBalance.subtract(transaction.getTotalTransactionAmount());
                    BigDecimal newDestinationBalance = destinationBalance.add(transaction.getAmount());

                    transaction.setInicialBalanceFrom(sourceBalance);
                    transaction.setFinalBalanceFrom(newSourceBalance);
                    transaction.setUserIdFrom(sourceAccount.getUserId());
                    transaction.setHumanIdFrom(Long.valueOf(sourceAccount.getHumanId()));
                    sourceAccount.setBalance(newSourceBalance.toString());

                    transaction.setInicialBalanceTo(destinationBalance);
                    transaction.setFinalBalanceTo(newDestinationBalance);
                    transaction.setUserIdTo(destinationAccount.getUserId());
                    transaction.setHumanIdTo(Long.valueOf(destinationAccount.getHumanId()));
                    destinationAccount.setBalance(newDestinationBalance.toString());

                    return depositRepostory.save(sourceAccount)
                            .then(depositRepostory.save(destinationAccount))
                            .then(depositRepostory.findById(nuquiTechDepositIdFixedFee))
                            .flatMap(this::updateNuquiBalance)
                            .then(Mono.just(transaction));
                })
                .flatMap(this::updateTransactionToCompleted);
    }

    private Mono<? extends Transaction> updateTransactionToCompleted(Transaction transactionToUpdate) {
        transactionToUpdate.setStatus(TRANSACTION_STATUS_COMPLETED);
        return transactionRepository.save(transactionToUpdate)
                .doOnNext(send -> kafkaProducer.send(depositsTransfersTopic, send));
    }

    private Mono<? extends Deposit> updateNuquiBalance(Deposit nuquiTechDeposit) {
        nuquiTechDeposit.setBalance(new BigDecimal(nuquiTechDeposit.getBalance()).add(fixedFee).toString());
        return depositRepostory.save(nuquiTechDeposit);
    }

    //    @Scheduled(cron = "${nuqui.tech.deposit.cron.generateTransactionsFileByChron.time-to-run}")
    @Scheduled(fixedRate = 5000) // 5000 milliseconds = 5 seconds
    public void generateTransactionsFileByChron() {
        LocalDate today = LocalDate.now();
        logger.info("generateTransactionsFileByChron: {}", today);
        AtomicInteger count = new AtomicInteger(0);

        File directory = new File(fileLocation);
        if (!directory.exists()) directory.mkdirs();
        String completeFilePath = directory + "/today-transactions-" + System.currentTimeMillis() + ".csv";

        transactionRepository
                .findAllTransactionsByDate(today.getYear(), today.getMonth().getValue(), today.getDayOfMonth())
                .collectList()
                .subscribe(transactions -> {
                    generateCSV(transactions, completeFilePath);
                    kafkaProducer.send(generateTransactionsFileByChronTopic, completeFilePath);
                    logger.info("generateTransactionsFileByChron complete total transactions: {}", transactions.size());
                });
    }

    private void generateCSV(List<Transaction> transactions, String fileLocation) {
        try (FileWriter writer = new FileWriter(new File(fileLocation))) {
            // Write column names
            writer.append("id,deposit_id_from,timestamp,deposit_id_to,human_id_from,user_id_from,human_id_to,user_id_to,status,fee,fee_deposit_id_to,amount,total_transaction_amount,inicial_balance_from,final_balance_from,inicial_balance_to,final_balance_to\n");

            // Write rows
            for (Transaction transaction : transactions) {
                writer.append(String.valueOf(transaction.getId())).append(',')
                        .append(String.valueOf(transaction.getDepositIdFrom())).append(',')
                        .append(String.valueOf(transaction.getTimestamp())).append(',')
                        .append(String.valueOf(transaction.getDepositIdTo())).append(',')
                        .append(String.valueOf(transaction.getHumanIdFrom())).append(',')
                        .append(String.valueOf(transaction.getUserIdFrom())).append(',')
                        .append(String.valueOf(transaction.getHumanIdTo())).append(',')
                        .append(String.valueOf(transaction.getUserIdTo())).append(',')
                        .append(String.valueOf(transaction.getStatus())).append(',')
                        .append(String.valueOf(transaction.getFee())).append(',')
                        .append(String.valueOf(transaction.getFeeDepositIdTo())).append(',')
                        .append(String.valueOf(transaction.getAmount())).append(',')
                        .append(String.valueOf(transaction.getTotalTransactionAmount())).append(',')
                        .append(String.valueOf(transaction.getInicialBalanceFrom())).append(',')
                        .append(String.valueOf(transaction.getFinalBalanceFrom())).append(',')
                        .append(String.valueOf(transaction.getInicialBalanceTo())).append(',')
                        .append(String.valueOf(transaction.getFinalBalanceTo())).append('\n');
            }

            System.out.println("CSV file generated at " + fileLocation);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
