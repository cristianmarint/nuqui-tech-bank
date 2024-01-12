package co.nuqui.tech.msbatchprocess;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class MsbatchprocessApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsbatchprocessApplication.class, args);
    }

}
