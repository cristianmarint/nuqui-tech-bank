package co.nuqui.tech.mslogger.infrastructure;

import co.nuqui.tech.mslogger.domain.dto.Log;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepository extends ReactiveMongoRepository<Log, UUID> {
}
