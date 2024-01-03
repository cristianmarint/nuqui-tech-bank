package co.nuqui.tech.mslogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class MsLoggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLoggerApplication.class, args);
	}

}
