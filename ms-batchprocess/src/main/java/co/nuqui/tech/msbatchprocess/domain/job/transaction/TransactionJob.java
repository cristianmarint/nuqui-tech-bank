package co.nuqui.tech.msbatchprocess.domain.job.transaction;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TransactionJob {

    @Autowired
    private JobRepository jobRepository;

    @Qualifier("TransactionJobStep1ImportData")
    @Autowired
    private Step transactionJobStep1ImportData;

    @Autowired
    private TransactionJobStep1CompletionListener transactionJobStep1CompletionListener;

    @Bean
    public Job TransactionJobRunner() {
        JobBuilder jobBuilder = new JobBuilder("TransactionJobRunner", jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .listener(transactionJobStep1CompletionListener)
                .start(transactionJobStep1ImportData)
                .build();
    }
}
