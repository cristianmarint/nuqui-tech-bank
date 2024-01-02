package co.nuqui.tech.msdeposits.domain.service;

import co.nuqui.tech.msdeposits.domain.dto.Human;
import co.nuqui.tech.msdeposits.infrastructure.restclient.HumanRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanService {
    @Autowired
    private HumanRestClient humanRestClient;

    public Human findById(Long humanId) {
        Human byIdentification = humanRestClient.findById(humanId);
        byIdentification.setPassword(null);
        byIdentification.setUsername(null);
        return byIdentification;
    }
}
