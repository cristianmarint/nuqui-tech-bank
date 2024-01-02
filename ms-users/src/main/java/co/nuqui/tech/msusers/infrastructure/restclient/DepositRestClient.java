package co.nuqui.tech.msusers.infrastructure.restclient;

import co.nuqui.tech.msusers.domain.dto.Deposit;
import co.nuqui.tech.msusers.infrastructure.controller.GlobalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class DepositRestClient {
    @Value("${ms-deposits.search.endpoint}")
    private String endpoint;

    public Deposit[] findById(Long humanId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(endpoint)
                    .queryParam("humanId", humanId)
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, Deposit[].class);
        } catch (Exception e) {
            throw new GlobalException("HumanRestClient error " + e);
        }
    }
}
