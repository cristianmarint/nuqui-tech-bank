package co.nuqui.tech.msdeposits.infrastructure.persistance;

import co.nuqui.tech.msdeposits.domain.dto.Deposit;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepositRepostory extends ReactiveCrudRepository<Deposit, UUID> {
    Flux<Deposit> findByHumanId(@Param("humanIdentification") Long humanIdentification);

    Flux<Deposit> findByUserId(@Param("userId") String userId);
}