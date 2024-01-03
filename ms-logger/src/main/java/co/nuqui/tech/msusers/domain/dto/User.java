package co.nuqui.tech.msusers.domain.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;

    private Long humanId;

    private String token;

    private String email;

    private String username;

    private String password;

    private String status;

    private String recentActivity;

    private String deletedAt;
}
