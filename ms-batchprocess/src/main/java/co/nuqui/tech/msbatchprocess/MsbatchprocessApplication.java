package co.nuqui.tech.msbatchprocess;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class MsbatchprocessApplication {
	private JobBuilder jobBuilder;
	private StepBuilder stepBuilder;


    public static void main(String[] args) {
        SpringApplication.run(MsbatchprocessApplication.class, args);
    }

}
