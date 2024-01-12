package co.nuqui.tech.msbatchprocess.domain.job.monthlyreport;

import co.nuqui.tech.msbatchprocess.app.config.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MonthlyReportJobStep1CompletionListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(MonthlyReportJobStep1CompletionListener.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${nuqui.tech.kafka.batchprocess.account-report.created}")
    private String accountRepostCreatedTopic;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("MonthlyReportJobStep1CompletionListener - verify the results");

//            jdbcTemplate.query("SELECT id, status,batch_status FROM deposit_transactions_batch WHERE date_trunc('month', timestamp) = date_trunc('month', current_date)  " +
//                            "AND date_trunc('year', timestamp) = date_trunc('year', current_date) " +
//                            "AND  batch_status = '" + TRANSACTION_STATUS_BATCH_CREATED_PDF + "'",
//                    (rs, row) -> Transaction.builder()
//                            .id(rs.getString(1))
//                            .status(rs.getString(2))
//                            .batchStatus(rs.getString(3))
//                            .build()
//            ).forEach(transaction -> log.info("MonthlyReportJobStep1CompletionListener - Found transaction id: <" + transaction.getId() + "> with status <" + transaction.getStatus() + "> batch status <" + transaction.getBatchStatus() + ">"));
            log.info("MonthlyReportJobStep1CompletionListener - done verifying the results");
            kafkaProducer.send(accountRepostCreatedTopic, jobExecution.getStartTime());
        }
    }
}
