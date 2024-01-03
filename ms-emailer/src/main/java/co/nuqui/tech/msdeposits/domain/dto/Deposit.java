package co.nuqui.tech.msdeposits.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Deposit {
    private UUID id;

    private String accountNumber;

    private String humanId;

    private String userId;

    private String name;

    private String balance;
}
