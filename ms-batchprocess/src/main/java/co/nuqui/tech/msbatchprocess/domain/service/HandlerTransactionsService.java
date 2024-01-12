package co.nuqui.tech.msbatchprocess.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandlerTransactionsService {

    private static final Logger logger = LoggerFactory.getLogger(HandlerTransactionsService.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job importTransactionJob;

    public void handlerTransactionFileCreated(String fileLocation) throws JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        logger.info("handlerTransactionFileCreated [START] ");

        jobLauncher.run(importTransactionJob,
                new JobParametersBuilder().addString("inputFilePath", fileLocation
                ).toJobParameters());

        logger.info("handlerTransactionFileCreated [DONE] ");
    }
}
