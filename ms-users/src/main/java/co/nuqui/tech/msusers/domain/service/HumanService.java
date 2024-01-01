package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Human;
import co.nuqui.tech.msusers.infrastructure.restclient.HumanRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanService {
    @Autowired
    private HumanRestClient humanRestClient;

    public Human findByIdentification(Long humanId) {
        Human byIdentification = humanRestClient.findByIdentification(humanId);
        byIdentification.setPassword(null);
        byIdentification.setUsername(null);
        return byIdentification;
    }
}
