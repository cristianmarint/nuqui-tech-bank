package co.nuqui.tech.msbatchprocess.EJEMPLO.config;

import co.nuqui.tech.msbatchprocess.EJEMPLO.JobCompletionListener;
import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class SpringBatchConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private JobRepository jobRepository;

    @StepScope
    @Bean
    public FlatFileItemReader<Transaction> reader(@Value("#{jobParameters[inputFilePath]}") String filePath) {
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
    public JdbcBatchItemWriter<Transaction> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(
                        "INSERT INTO deposit_transactions_batch (" +
                                "transaction_id, deposit_id_from, timestamp, deposit_id_to, human_id_from, user_id_from, human_id_to, user_id_to, status, fee, " +
                                "fee_deposit_id_to, amount, total_transaction_amount, inicial_balance_from, final_balance_from, inicial_balance_to, " +
                                "final_balance_to) " +
                                "VALUES  (" +
                                "UUID(:id), UUID(:depositIdFrom), TO_TIMESTAMP(REPLACE(REPLACE(:timestamp, 'T', ' '), 'Z', ''), 'YYYY-MM-DD HH24:MI:SS.US'), UUID(:depositIdTo), :humanIdFrom, :userIdFrom, :humanIdTo, :userIdTo,  :status, :fee, " +
                                "UUID(:feeDepositIdTo), :amount, :totalTransactionAmount, :inicialBalanceFrom, :finalBalanceFrom, :inicialBalanceTo, " +
                                ":finalBalanceTo)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTransactionJob(JobCompletionListener listener, Step step1Transaction) {
        JobBuilder jobBuilder = new JobBuilder("importTransactionJob", jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1Transaction)
                .build();
    }

    @Bean
    public Step step1Transaction(JdbcBatchItemWriter<Transaction> writer, ItemProcessor processor, FlatFileItemReader<Transaction> reader) {
        StepBuilder stepBuilder = new StepBuilder("step1Transaction", jobRepository);
        return stepBuilder
                .chunk(2, transactionManager) // replace transactionManager with your actual transaction manager
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
