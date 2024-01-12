package co.nuqui.tech.msbatchprocess.EJEMPLO;

import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionItemProcessor.class);

    @Override
    public Transaction process(final Transaction transaction) {
        transaction.setStatus("PROCESSED");
        log.info("Processing transaction: " + transaction.toString());
        return transaction;
    }

}