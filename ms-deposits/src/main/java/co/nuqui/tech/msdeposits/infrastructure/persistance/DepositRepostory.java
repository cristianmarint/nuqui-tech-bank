package co.nuqui.tech.msdeposits.infrastructure.persistance;

import co.nuqui.tech.msdeposits.domain.dto.Deposit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepositRepostory extends ReactiveCrudRepository<Deposit, UUID> {
}