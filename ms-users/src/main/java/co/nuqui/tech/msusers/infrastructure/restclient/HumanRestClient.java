package co.nuqui.tech.msusers.infrastructure.restclient;

import co.nuqui.tech.msusers.domain.dto.Human;
import co.nuqui.tech.msusers.infrastructure.controller.GlobalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HumanRestClient {
    @Value("${ms-humans.search.endpoint}")
    private String endpoint;

    public Human findByIdentification(Long humanId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(endpoint + humanId, Human.class);
        } catch (Exception e) {
            throw new GlobalException("HumanRestClient error " + e);
        }
    }
}
