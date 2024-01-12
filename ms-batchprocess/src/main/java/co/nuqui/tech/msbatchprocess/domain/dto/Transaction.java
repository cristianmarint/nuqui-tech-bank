package co.nuqui.tech.msbatchprocess.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "deposit_transactions_batch")
public class Transaction {

    @Id
    @Column("id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @Column("deposit_id_from")
    @NotNull
    private String depositIdFrom;

    @CreatedDate
    @Column("timestamp")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String timestamp;

    @Column("deposit_id_to")
    @NotNull
    private String depositIdTo;

    @Column("human_id_from")
    private String humanIdFrom;

    @Column("user_id_from")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userIdFrom;

    @Column("human_id_to")
    @JsonIgnore
    private String humanIdTo;

    @Column("user_id_to")
    @JsonIgnore
    private String userIdTo;

    @Column("status")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @Column("fee")
    private String fee;

    @Column("fee_deposit_id_to")
    @JsonIgnore
    private String feeDepositIdTo;

    @Column("amount")
    private String amount;

    @Column("total_transaction_amount")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String totalTransactionAmount;

    @Column("inicial_balance_from")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String inicialBalanceFrom;

    @Column("final_balance_from")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String finalBalanceFrom;

    @Column("inicial_balance_to")
    @JsonIgnore
    private String inicialBalanceTo;

    @Column("final_balance_to")
    @JsonIgnore
    private String finalBalanceTo;

    @Column("file_location")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String fileLocation;
}
