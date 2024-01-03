package co.nuqui.tech.msusers.domain.dto;

import co.nuqui.tech.msdeposits.domain.dto.Deposit;
import co.nuqui.tech.mshumans.domain.dto.Human;
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
