package co.nuqui.tech.msdeposits.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("transactions")
public class Transaction {

    @Id
    @Column("id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column("deposit_id_from")
    @NotNull
    private UUID depositIdFrom;

    @CreatedDate
    @Column("timestamp")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant timestamp;

    @Column("deposit_id_to")
    @NotNull
    private UUID depositIdTo;

    @Column("human_id_from")
    private Long humanIdFrom;

    @Column("user_id_from")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userIdFrom;

    @Column("human_id_to")
    @JsonIgnore
    private Long humanIdTo;

    @Column("user_id_to")
    @JsonIgnore
    private String userIdTo;

    @Column("status")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @Column("fee")
    private BigDecimal fee;

    @Column("fee_deposit_id_to")
    @JsonIgnore
    private UUID feeDepositIdTo;

    @Column("amount")
    private BigDecimal amount;

    @Column("total_transaction_amount")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalTransactionAmount;

    @Column("inicial_balance_from")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal inicialBalanceFrom;

    @Column("final_balance_from")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal finalBalanceFrom;

    @Column("inicial_balance_to")
    @JsonIgnore
    private BigDecimal inicialBalanceTo;

    @Column("final_balance_to")
    @JsonIgnore
    private BigDecimal finalBalanceTo;
}
