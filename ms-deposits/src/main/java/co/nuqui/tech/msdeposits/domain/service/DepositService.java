package co.nuqui.tech.msdeposits.domain.service;

import co.nuqui.tech.msdeposits.domain.dto.Deposit;
import co.nuqui.tech.msdeposits.domain.dto.Transaction;
import co.nuqui.tech.msdeposits.infrastructure.persistance.DepositRepostory;
import co.nuqui.tech.msdeposits.infrastructure.persistance.TransactonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DepositService {

    @Autowired
    private DepositRepostory depositRepostory;

    @Autowired
    private TransactonRepository transactonRepository;

    public Mono<Deposit> save(Mono<Deposit> deposit) {
        return deposit.flatMap(depositRepostory::save);
    }

    public Flux<Transaction> findAllTransactionsByAccountFrom(UUID depositIdFrom, int limit, int offset) {
        return transactonRepository.findAllTransactionsByDepositIdFrom(depositIdFrom, limit, offset);
    }
}
