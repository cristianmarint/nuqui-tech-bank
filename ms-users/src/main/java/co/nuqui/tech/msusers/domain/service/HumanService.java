package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Human;
import co.nuqui.tech.msusers.infrastructure.restclient.HumanRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanService {
    @Autowired
    private HumanRestClient humanRestClient;

    public Human findById(Long humanId) {
        Human byId = humanRestClient.findById(humanId);
        byId.setPassword(null);
        byId.setUsername(null);
        return byId;
    }
}
