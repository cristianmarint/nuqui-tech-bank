package co.nuqui.tech.msbatchprocess.domain.job.transaction;

import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static co.nuqui.tech.msbatchprocess.domain.dto.TransactionConstants.TRANSACTION_STATUS_BATCH_IMPORTED;

@Component(value = "TransactionJobStep1ItemProcessor")
public class TransactionJobStep1ItemProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionJobStep1ItemProcessor.class);

    @Override
    public Transaction process(final Transaction transaction) {
        transaction.setBatchStatus(TRANSACTION_STATUS_BATCH_IMPORTED);
        log.info("Transaction Job TransactionJobStep1ItemProcessor - transaction id {} batch status {} ",transaction.getId(),transaction.getBatchStatus());
        return transaction;
    }

}