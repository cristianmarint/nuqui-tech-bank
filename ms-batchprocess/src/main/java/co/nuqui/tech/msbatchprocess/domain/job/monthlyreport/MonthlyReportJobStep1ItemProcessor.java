package co.nuqui.tech.msbatchprocess.domain.job.monthlyreport;

import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component(value = "MonthlyReportJobStep1ItemProcessor")
public class MonthlyReportJobStep1ItemProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LoggerFactory.getLogger(MonthlyReportJobStep1ItemProcessor.class);

    @Override
    public Transaction process(Transaction transaction) {
        log.info("MonthlyReport Processor - transaction {} ", transaction);
        return transaction;
    }

}