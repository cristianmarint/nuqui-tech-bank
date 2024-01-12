package co.nuqui.tech.msbatchprocess.app.config;

import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import co.nuqui.tech.msbatchprocess.domain.service.HandlerTransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private HandlerTransactionsService service;

    @KafkaListener(topics = "deposits.transaction.file.created", groupId = "nuqui.tech")
    public void flightEventConsumer(@Payload String fileLocation) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("Kafka event deposits.transaction.file.created message -> {}", fileLocation);
        service.handlerTransactionFileCreated(fileLocation);
    }
}
