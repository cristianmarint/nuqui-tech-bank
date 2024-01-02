package co.nuqui.tech.msdeposits.infrastructure.persistance;

import co.nuqui.tech.msdeposits.domain.dto.Transaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface TransactonRepository extends ReactiveCrudRepository<Transaction, UUID> {
    @Query(value = "SELECT * FROM transactions WHERE deposit_id_from = :depositIdFrom LIMIT :limit OFFSET :offset")
    Flux<Transaction> findAllTransactionsByDepositIdFrom(UUID depositIdFrom, int limit, int offset);
}