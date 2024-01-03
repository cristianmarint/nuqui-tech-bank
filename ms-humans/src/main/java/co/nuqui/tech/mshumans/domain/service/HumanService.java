package co.nuqui.tech.mshumans.domain.service;

import co.nuqui.tech.mshumans.domain.dto.Human;
import co.nuqui.tech.mshumans.infrastructure.controller.GlobalException;
import co.nuqui.tech.mshumans.infrastructure.persistance.HumanRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class HumanService {

    private static final Logger logger = LoggerFactory.getLogger(HumanService.class);

    @Autowired
    private HumanRepository humanRepository;

    @Autowired
    private HumanEventPublisher humanEventPublisher;

    @Value("${spring.rabbitmq.human.created.routing-key}")
    private String humanCreatedRoutingKey;

    @Value("${spring.rabbitmq.human.search.routing-key}")
    private String humanSearchRoutingKey;

    public Human save(Human human) {
        logger.info("save human: {}", human);
        humanRepository.save(human);
        humanEventPublisher.publish(humanCreatedRoutingKey,human);
        return human;
    }

    public Human findByIdentification(String identification) {
        logger.info("find human by identification: {}", identification);
        Human human = humanRepository.findByIdentification(identification);
        humanEventPublisher.publish(humanSearchRoutingKey,human);
        return human;
    }

    public Human findById(Long id) {
        logger.info("find human by id: {}", id);
        Human human = humanRepository.findById(id).orElse(null);
        humanEventPublisher.publish(humanSearchRoutingKey,human);
        return human;
    }

    public Human findByIdentificationOrId(String identification, Long id) {
        if ((identification == null || identification.isEmpty()) && id == null)
            throw new GlobalException("Either identification or id is required");
        if (identification != null && id != null)
            throw new GlobalException("Identification and id are not allowed at the same time");
        if (identification != null) return findByIdentification(identification);
        return findById(id);
    }
}
