package co.nuqui.tech.mshumans.domain.service;

import co.nuqui.tech.mshumans.domain.dto.Human;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HumanEventPublisher {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.human.exchange}")
    private String humanExchange;

    @Value("${spring.rabbitmq.human.default-receive-queue}")
    private String humanQueue;

    @Value("${spring.rabbitmq.human.routing-key}")
    private String humanRoutingKey;

    public HumanEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Human human) {
        rabbitTemplate.convertAndSend(humanExchange, humanRoutingKey, human);
    }
}
