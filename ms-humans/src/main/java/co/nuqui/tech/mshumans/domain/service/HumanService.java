package co.nuqui.tech.mshumans.domain.service;

import co.nuqui.tech.mshumans.domain.dto.Human;
import co.nuqui.tech.mshumans.infrastructure.controller.GlobalException;
import co.nuqui.tech.mshumans.infrastructure.persistance.HumanRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Human save(Human human) {
        logger.info("save human: {}", human);
        humanRepository.save(human);
        humanEventPublisher.publish(human);
        return human;
    }

    public Human findByIdentification(String identification) {
        logger.info("find human by identification: {}", identification);
        return humanRepository.findByIdentification(identification);
    }

    public Human findById(Long id) {
        logger.info("find human by id: {}", id);
        return humanRepository.findById(id).orElse(null);
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
