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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static co.nuqui.tech.msdeposits.domain.dto.DepositConstants.DEFAULT_NAME;
import static co.nuqui.tech.msdeposits.domain.dto.TransferConstants.*;

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

    @Value("${nuqui.tech.kafka.deposits.transfer.topic}")
    private String depositsTransfersTopic;

    public Mono<Deposit> save(Mono<Deposit> deposit) {
        return deposit.flatMap(depositRepostory::save);
    }

    public Flux<Transaction> findAllTransactionsByAccountFrom(UUID depositIdFrom, int limit, int offset) {
        return transactionRepository
                .findAllTransactionsByDepositIdFrom(depositIdFrom, limit, offset)
                .doOnNext(transaction -> kafkaProducer.send(depositsTransactionsTopic,transaction));
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
                    .doOnNext(transaction -> kafkaProducer.send(depositsSearchTopic,transaction)).block();
        }

        logger.info("find deposits by userId: {}", userId);
        return depositRepostory
                .findByUserId(userId).collectList()
                .doOnNext(transaction -> kafkaProducer.send(depositsSearchTopic,transaction)).block();

    }

    public Mono<Transaction> transfer(Transaction transaction) {
        transaction.setStatus(TRANSACTION_STATUS_PROCESSING);

        return transactionRepository.save(transaction)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(tuple -> {
                    Deposit sourceAccount = depositRepostory.findById(transaction.getDepositIdFrom()).block();
                    Deposit destinationAccount = depositRepostory.findById(transaction.getDepositIdTo()).block();

                    BigDecimal sourceBalance = new BigDecimal(sourceAccount.getBalance());
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
                .doOnNext(transaction1 -> kafkaProducer.send(depositsTransfersTopic,transaction1))
                .flatMap(this::updateTransactionToCompleted);
    }

    private Mono<? extends Transaction> updateTransactionToCompleted(Transaction transactionToUpdate) {
        transactionToUpdate.setStatus(TRANSACTION_STATUS_COMPLETED);
        return transactionRepository.save(transactionToUpdate);
    }

    private Mono<? extends Deposit> updateNuquiBalance(Deposit nuquiTechDeposit) {
        nuquiTechDeposit.setBalance(new BigDecimal(nuquiTechDeposit.getBalance()).add(fixedFee).toString());
        return depositRepostory.save(nuquiTechDeposit);
    }
}
