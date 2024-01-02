package co.nuqui.tech.msdeposits.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("deposits")
public class Deposit {

    @Id
    @Column("id")
    private UUID id;

    @Column("account_number")
    private String accountNumber;

    @Column("human_id")
    private String humanId;

    @Column("user_id")
    private String userId;

    @Column("name")
    private String name;

    @Column("balance")
    private String balance;
}
