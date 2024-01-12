package co.nuqui.tech.msbatchprocess.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobsHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(JobsHandlerService.class);

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("TransactionJobRunner")
    @Autowired
    Job transactionJobRunner;

    @Qualifier("MonthlyReportJobRunner")
    @Autowired
    Job monthlyReportJobRunner;

    public void handlerTransactionFileJob(String batchFileCSV) throws JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        logger.info("handlerTransactionFileCreated [START] ");

        jobLauncher.run(transactionJobRunner,
                new JobParametersBuilder().addString("inputFilePath", batchFileCSV
                ).toJobParameters());

        logger.info("handlerTransactionFileCreated [DONE] ");
    }


    //    @Scheduled(cron = "${nuqui.tech.deposit.cron.generateMonthlyReportFileByChron.time-to-run}")
    @Scheduled(fixedRate = 5000) // 5000 milliseconds = 5 seconds
    public void handlerMonthlyReportJob() throws JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        logger.info("handlerMonthlyReportJob [START] ");
        jobLauncher.run(monthlyReportJobRunner, new JobParameters());
        logger.info("handlerMonthlyReportJob [DONE] ");
    }
}
