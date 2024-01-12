package co.nuqui.tech.msbatchprocess.app.config;

import co.nuqui.tech.msbatchprocess.domain.service.JobsHandlerService;
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
    private JobsHandlerService service;

    @KafkaListener(topics = "deposits.transaction.file.created", groupId = "nuqui.tech")
    public void flightEventConsumer(@Payload String batchFileCSV) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("Kafka event deposits.transaction.file.created message -> {}", batchFileCSV);
        service.handlerTransactionFileJob(batchFileCSV);
    }
}
