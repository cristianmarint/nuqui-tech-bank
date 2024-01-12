package co.nuqui.tech.msbatchprocess.domain.job.transaction;

import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransactionJobStep1CompletionListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(TransactionJobStep1CompletionListener.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Transaction Job TransactionJobStep1CompletionListener - verify the results");
            LocalDate today = LocalDate.now();

            jdbcTemplate.query("SELECT id, status FROM deposit_transactions_batch WHERE date_trunc('day', timestamp) = to_date( "+today.getYear()+" || '-' || "+today.getMonthValue()+" || '-' || "+today.getDayOfMonth()+", 'YYYY-MM-DD')",
                    (rs, row) -> Transaction.builder()
                            .id(rs.getString(1))
                            .status(rs.getString(2))
                            .build()
            ).forEach(transaction -> log.info("Transaction Job TransactionJobStep1CompletionListener - Found transaction id: <" + transaction.getId() + "> with status <" + transaction.getStatus() + ">"));
        }
    }
}
