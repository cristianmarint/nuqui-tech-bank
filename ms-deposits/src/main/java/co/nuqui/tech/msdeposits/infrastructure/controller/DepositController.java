package co.nuqui.tech.msdeposits.infrastructure.controller;

import co.nuqui.tech.msdeposits.domain.dto.Deposit;
import co.nuqui.tech.msdeposits.domain.dto.Transaction;
import co.nuqui.tech.msdeposits.domain.service.DepositService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static co.nuqui.tech.msdeposits.infrastructure.controller.Mappings.URL_DEPOSITS_V1;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

@RestController
@RequestMapping(value = URL_DEPOSITS_V1)
public class DepositController {

    @Autowired
    private DepositService depositService;

//    @ExceptionHandler(GlobalException.class)
    @PostMapping(path = "/save", produces = APPLICATION_STREAM_JSON_VALUE)
    public Mono<Deposit> save(@Valid @RequestBody Mono<Deposit> depositMono) {
        return depositService.save(depositMono);
    }

//    @ExceptionHandler(GlobalException.class)
    @GetMapping(value = "/transactions", produces = APPLICATION_STREAM_JSON_VALUE)
    Flux<Transaction> getTransactions(
            @RequestParam(value = "account-from-id", defaultValue = "1") UUID accountFromId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int limit
    ) {
        int offset = (page - 1) * limit;
        return depositService
                .findAllTransactionsByAccountFrom(accountFromId, limit, offset)
                .delayElements(Duration.ofSeconds(1L));
    }

    //    @ExceptionHandler(GlobalException.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/search/")
    public ResponseEntity<List<Deposit>> findByHumanIdOrUserId(
            @Param("humanId") Long humanId,
            @Param("userId") String userId
    ) {
        return ResponseEntity.ok().body(
                depositService.findByHumanIdOrUserId(humanId, userId)
        );
    }

    @ExceptionHandler(GlobalException.class)
    @PostMapping("transfer")
    public Mono<ResponseEntity<Transaction>> transfer(@RequestBody Transaction transaction) {
        return depositService.transfer(transaction)
                .map(response -> ResponseEntity.status(OK).body(response));
    }
}
