package co.nuqui.tech.msusers.domain.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;

    private Long humanId;

    @JsonIgnore
    private String token;

    private String email;

    private String username;

    private String password;

    private String status;

    private String recentActivity;

    @JsonIgnore
    private String deletedAt;
}
