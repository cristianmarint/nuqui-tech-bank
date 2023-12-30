package co.nuqui.tech.mshumans.domain.service;

import co.nuqui.tech.mshumans.domain.dto.Human;
import co.nuqui.tech.mshumans.infrastructure.persistance.HumanRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class HumanService {

    @Autowired
    private HumanRepository humanRepository;

    @Autowired
    private HumanEventPublisher humanEventPublisher;

    public Human save(Human human) {
        humanRepository.save(human);
        humanEventPublisher.publish(human);
        return human;
    }

    public Human findByIdentification(String identification) {
        return humanRepository.findByIdentification(identification);
    }
}
