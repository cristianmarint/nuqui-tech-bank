package co.nuqui.tech.msdeposits.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    private UUID id;

    private UUID depositIdFrom;

    private Instant timestamp;

    private UUID depositIdTo;

    private Long humanIdFrom;

    private String userIdFrom;

    private Long humanIdTo;

    private String userIdTo;

    private String status;

    private BigDecimal fee;

    private UUID feeDepositIdTo;

    private BigDecimal amount;

    private BigDecimal totalTransactionAmount;

    private BigDecimal inicialBalanceFrom;

    private BigDecimal finalBalanceFrom;

    private BigDecimal inicialBalanceTo;

    private BigDecimal finalBalanceTo;
}
