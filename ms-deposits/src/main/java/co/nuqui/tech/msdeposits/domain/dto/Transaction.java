package co.nuqui.tech.msdeposits.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@Table("transactions")
public class Transaction {

    @Id
    @Column("id")
    private UUID id;

    @Column("deposit_id_from")
    private UUID depositIdFrom;

    @Column("deposit_id_to")
    private UUID depositIdTo;

    @Column("human_id_from")
    private Long humanIdFrom;

    @Column("user_id_from")
    private String userIdFrom;

    @Column("human_id_to")
    private Long humanIdTo;

    @Column("user_id_to")
    private String userIdTo;

    @Column("fee")
    private BigDecimal fee;

    @Column("amount")
    private BigDecimal amount;

    @Column("total_transaction_amount")
    private BigDecimal totalTransactionAmount;

    @Column("inicial_balance_from")
    private BigDecimal inicialBalanceFrom;

    @Column("final_balance_from")
    private BigDecimal finalBalanceFrom;

    @Column("inicial_balance_to")
    private BigDecimal inicialBalanceTo;

    @Column("final_balance_to")
    private BigDecimal finalBalanceTo;
}
