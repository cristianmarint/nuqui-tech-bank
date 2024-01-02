package co.nuqui.tech.msusers.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Deposit {

    private UUID id;

    private String accountNumber;

    private String humanId;

    private String userId;

    private String name;

    private String balance;
}
