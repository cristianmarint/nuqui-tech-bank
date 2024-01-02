package co.nuqui.tech.msdeposits.infrastructure.restclient;

import co.nuqui.tech.msdeposits.domain.dto.Human;
import co.nuqui.tech.msdeposits.infrastructure.controller.GlobalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class HumanRestClient {
    @Value("${ms-humans.search.endpoint}")
    private String endpoint;

    public Human findById(Long humanId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(endpoint)
                    .queryParam("id", humanId)
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, Human.class);
        } catch (Exception e) {
            throw new GlobalException("HumanRestClient error " + e);
        }
    }
}
