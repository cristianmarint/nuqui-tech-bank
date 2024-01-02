package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Deposit;
import co.nuqui.tech.msusers.infrastructure.restclient.DepositRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {

    @Autowired
    private DepositRestClient depositRestClient;

    public List<Deposit> findByHumanId(Long humanId) {
        return List.of(depositRestClient.findById(humanId));
    }
}
