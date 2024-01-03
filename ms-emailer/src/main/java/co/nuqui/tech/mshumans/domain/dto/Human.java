package co.nuqui.tech.mshumans.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Human {
    private Long id;

    private String name;

    private String lastname;

    private String identification;

    private String email;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String password;
}
