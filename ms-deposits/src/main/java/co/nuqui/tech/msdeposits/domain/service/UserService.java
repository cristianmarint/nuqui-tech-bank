package co.nuqui.tech.msdeposits.domain.service;

import co.nuqui.tech.msdeposits.domain.dto.Me;
import co.nuqui.tech.msdeposits.infrastructure.restclient.UserRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    private UserRestClient userRestClient;

    public Mono<Me> findById(String humanId) {
        return userRestClient.findByEmail(humanId);
    }
}
