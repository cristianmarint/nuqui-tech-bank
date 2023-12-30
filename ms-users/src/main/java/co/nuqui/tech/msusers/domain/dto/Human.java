package co.nuqui.tech.msusers.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
    @Nullable
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    @NotNull
    private String identification;

    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String password;
}
