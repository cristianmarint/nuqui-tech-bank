package co.nuqui.tech.msdeposits.infrastructure.restclient;

import co.nuqui.tech.msdeposits.domain.dto.Me;
import co.nuqui.tech.msdeposits.domain.dto.User;
import co.nuqui.tech.msdeposits.infrastructure.controller.GlobalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserRestClient {
    @Value("${ms-users.me.endpoint}")
    private String endpoint;

    public Mono<Me> findByEmail(String email) {
        try {
            WebClient webClient = WebClient.create(endpoint);

            User build = User.builder()
                    .email(email)
                    .build();

            return webClient.post()
                    .body(Mono.just(build), User.class)
                    .retrieve()
                    .bodyToMono(Me.class);
        } catch (Exception e) {
            return Mono.error(new GlobalException("UserRestClient error " + e));
        }
    }
}
