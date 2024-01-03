package co.nuqui.tech.mshumans.domain.service;

import co.nuqui.tech.mshumans.domain.dto.Human;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HumanEventPublisher {

    private Logger logger = LoggerFactory.getLogger(HumanEventPublisher.class);

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.human.exchange}")
    private String humanExchange;


    public HumanEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey,Human human) {
        logger.info("publish event routingKey: {},  {}", routingKey, human);
        rabbitTemplate.convertAndSend(humanExchange, routingKey, human);
    }
}
