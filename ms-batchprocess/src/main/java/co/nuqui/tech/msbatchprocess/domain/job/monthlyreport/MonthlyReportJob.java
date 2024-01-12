package co.nuqui.tech.msbatchprocess.domain.job.monthlyreport;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlyReportJob {

    @Autowired
    private JobRepository jobRepository;

    @Qualifier("MonthlyReportJobStep1CreatePDF")
    @Autowired
    private Step monthlyReportJobStep1CreatePDF;

    @Autowired
    private MonthlyReportJobStep1CompletionListener monthlyReportJobStep1CompletionListener;

    @Bean
    public Job MonthlyReportJobRunner() {
        JobBuilder jobBuilder = new JobBuilder("MonthlyReportJobRunner", jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .listener(monthlyReportJobStep1CompletionListener)
                .start(monthlyReportJobStep1CreatePDF)
                .build();
    }
}
