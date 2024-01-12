package co.nuqui.tech.msbatchprocess.domain.job.monthlyreport;

import co.nuqui.tech.msbatchprocess.domain.dto.Transaction;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.StepBuilderHelper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.nuqui.tech.msbatchprocess.domain.dto.TransactionConstants.TRANSACTION_STATUS_BATCH_CREATED_PDF;
import static co.nuqui.tech.msbatchprocess.domain.dto.TransactionConstants.TRANSACTION_STATUS_BATCH_IMPORTED;

@SuppressWarnings({"rawtypes", "unchecked"})
@Component
public class MonthlyReportJobStep1 {
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("MonthlyReportJobStep1ReaderDB")
    @Autowired
    private JdbcCursorItemReader<Transaction> monthlyReportJobStep1ReaderDB;

    @Qualifier("MonthlyReportJobStep1ItemProcessor")
    @Autowired
    private ItemProcessor monthlyReportJobStep1ItemProcessor;

    @Qualifier("MonthlyReportJobStep1WriterPDF")
    @Autowired
    private ItemWriter monthlyReportJobStep1WriterPDF;

    private Logger logger = LoggerFactory.getLogger(MonthlyReportJobStep1.class);


    /**
     * Reads DB for monthly transactions and creates PDF files
     */
    @Bean(name = "MonthlyReportJobStep1CreatePDF")
    public Step MonthlyReportJobStep1CreatePDF() {
        StepBuilder stepBuilder = new StepBuilder("MonthlyReportJobStep1CreatePDF", jobRepository);
        stepBuilder.allowStartIfComplete(true);
        return stepBuilder
                .chunk(100, transactionManager)
                .reader(monthlyReportJobStep1ReaderDB)
                .processor(monthlyReportJobStep1ItemProcessor)
                .writer(monthlyReportJobStep1WriterPDF)
                .build();
    }

    private RowMapper<Transaction> transactionRowMapper() {
        logger.info("transactionRowMapper method called");

        return (rs, rowNum) -> {
            Transaction transaction = Transaction.builder().id(rs.getString("id")).transactionId(rs.getString("transaction_id")).depositIdFrom(rs.getString("deposit_id_from")).depositIdTo(rs.getString("deposit_id_to")).status(rs.getString("status")).batchStatus(rs.getString("batch_status")).timestamp(rs.getString("timestamp")).humanIdFrom(rs.getString("human_id_from")).userIdFrom(rs.getString("user_id_from")).humanIdTo(rs.getString("human_id_to")).userIdTo(rs.getString("user_id_to")).fee(rs.getString("fee")).feeDepositIdTo(rs.getString("fee_deposit_id_to")).amount(rs.getString("amount")).totalTransactionAmount(rs.getString("total_transaction_amount")).inicialBalanceFrom(rs.getString("inicial_balance_from")).finalBalanceFrom(rs.getString("final_balance_from")).inicialBalanceTo(rs.getString("inicial_balance_to")).finalBalanceTo(rs.getString("final_balance_to")).build();
            logger.info("MonthlyReportJobStep1CreatePDF  transactionRowMapper - {}", transaction);
            return transaction;
        };
    }

    @Bean(name = "MonthlyReportJobStep1ReaderDB")
    public JdbcCursorItemReader<Transaction> MonthlyReportJobStep1ReaderDB(DataSource dataSource) {
        logger.info("MonthlyReportJobStep1ReaderDB method called");
        JdbcCursorItemReader<Transaction> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM deposit_transactions_batch WHERE date_trunc('month', timestamp) = date_trunc('month', current_date) AND date_trunc('year', timestamp) = date_trunc('year', current_date) AND deposit_transactions_batch.batch_status = '" + TRANSACTION_STATUS_BATCH_IMPORTED + "'");
        reader.setRowMapper(transactionRowMapper());
        return reader;
    }

    @Bean(name = "MonthlyReportJobStep1WriterPDF")
    public ItemWriter<Transaction> MonthlyReportJobStep1WriterPDF(@Value("${nuqui.tech.kafka.deposits.transaction.file.pdf.location}") String fileLocation) {
        File directory = new File(fileLocation);
        if (!directory.exists()) directory.mkdirs();

        return chunk -> {
            List<Transaction> transactions = (List<Transaction>) chunk.getItems();
            Map<String, List<Transaction>> transactionsByDepositIdFrom = transactions.stream().collect(Collectors.groupingBy(Transaction::getDepositIdFrom));

            for (Map.Entry<String, List<Transaction>> entry : transactionsByDepositIdFrom.entrySet()) {
                String depositIdFrom = entry.getKey();
                List<Transaction> userTransactions = entry.getValue();

                String completeFilePath = fileLocation + "/montly-transactions-by-deposit-id-from-" + depositIdFrom + "-" + System.currentTimeMillis() + ".pdf";

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(completeFilePath));
                document.open();

                PdfPTable table = new PdfPTable(10);

                // Add column headers
                table.addCell("Timestamp");
                table.addCell("Transaction ID");
                table.addCell("Deposit ID From");
                table.addCell("Deposit ID To");
                table.addCell("Status");

                table.addCell("Fee");
                table.addCell("Amount");
                table.addCell("Total Transaction Amount");
                table.addCell("InicialBalance From");
                table.addCell("Final Balance From");

                for (Transaction transaction : userTransactions) {
                    table.addCell(transaction.getTimestamp());
                    table.addCell(transaction.getTransactionId());
                    table.addCell(transaction.getDepositIdFrom());
                    table.addCell(transaction.getDepositIdTo());
                    table.addCell(transaction.getStatus());
                    table.addCell(transaction.getFee());
                    table.addCell(transaction.getAmount());
                    table.addCell(transaction.getTotalTransactionAmount());
                    table.addCell(transaction.getInicialBalanceFrom());
                    table.addCell(transaction.getFinalBalanceFrom());

                    jdbcTemplate.update("UPDATE deposit_transactions_batch SET batch_status = ?, batch_file_pdf=?  WHERE id = ?", TRANSACTION_STATUS_BATCH_CREATED_PDF,completeFilePath, UUID.fromString(transaction.getId()));
                }

                document.add(table);
                document.close();
            }
        };
    }
}


