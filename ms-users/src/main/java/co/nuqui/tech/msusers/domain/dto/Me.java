package co.nuqui.tech.msusers.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Me {
    private User user;
    private Human human;
    private List<Deposit> deposits;
}
