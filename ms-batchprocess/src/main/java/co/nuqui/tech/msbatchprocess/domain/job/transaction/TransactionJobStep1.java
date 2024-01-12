package co.nuqui.tech.msbatchprocess.domain.job.transaction;

import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SuppressWarnings({"unchecked", "rawtypes"})
@Component
public class TransactionJobStep1 {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private FlatFileItemReader<Transaction> transactionJobStep1ReaderCSV;

    @Qualifier("TransactionJobStep1ItemProcessor")
    @Autowired
    private ItemProcessor transactionJobStep1ItemProcessor;

    @Autowired
    private JdbcBatchItemWriter<Transaction> transactionJobStep1WriterDB;


    /**
     * Import CSV  from kafka event deposits.transaction.file.created and save to DB
     */
    @Bean
    public Step TransactionJobStep1ImportData() {
        StepBuilder stepBuilder = new StepBuilder("TransactionJobStep1ImportData", jobRepository);
        return stepBuilder
                .chunk(2, transactionManager)
                .reader(transactionJobStep1ReaderCSV)
                .processor(transactionJobStep1ItemProcessor)
                .writer(transactionJobStep1WriterDB)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Transaction> TransactionJobStep1ReaderCSV(@Value("#{jobParameters[inputFilePath]}") String filePath) {
        FileSystemResource fileSystemResource = new FileSystemResource(filePath.replace("..\\", ""));
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("ItemReaderTransaction")
                .delimited()
                .names(new String[]{
                        "id", "deposit_id_from", "timestamp", "deposit_id_to", "human_id_from", "user_id_from",
                        "human_id_to", "user_id_to", "status", "fee", "fee_deposit_id_to", "amount",
                        "total_transaction_amount", "inicial_balance_from", "final_balance_from",
                        "inicial_balance_to", "final_balance_to" // removed "file_location"
                })
                .resource(fileSystemResource)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                    setTargetType(Transaction.class);
                }})
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> TransactionJobStep1WriterDB(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(
                        "INSERT INTO deposit_transactions_batch (" +
                                "transaction_id, deposit_id_from, timestamp, deposit_id_to, human_id_from, user_id_from, human_id_to, user_id_to, batch_status, status, fee, " +
                                "fee_deposit_id_to, amount, total_transaction_amount, inicial_balance_from, final_balance_from, inicial_balance_to, " +
                                "final_balance_to) " +
                                "VALUES  (" +
                                "UUID(:id), UUID(:depositIdFrom), TO_TIMESTAMP(REPLACE(REPLACE(:timestamp, 'T', ' '), 'Z', ''), 'YYYY-MM-DD HH24:MI:SS.US'), " +
                                "UUID(:depositIdTo), :humanIdFrom, :userIdFrom, :humanIdTo, :userIdTo, :batchStatus ,:status, :fee, UUID(:feeDepositIdTo), :amount, :totalTransactionAmount, " +
                                ":inicialBalanceFrom, :finalBalanceFrom, :inicialBalanceTo, :finalBalanceTo)")
                .dataSource(dataSource)
                .build();
    }
}
