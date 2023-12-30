package co.nuqui.tech.mshumans.domain.service;

import co.nuqui.tech.mshumans.domain.dto.human.Human;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String defaultReceiveQueue;
    private final String routingKey;

    @Autowired
    public HumanEventPublisher(RabbitTemplate rabbitTemplate,
                               String exchange,
                               String defaultReceiveQueue,
                               String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.defaultReceiveQueue = defaultReceiveQueue;
        this.routingKey = routingKey;
    }

    public void publish(Human human) {
        rabbitTemplate.convertAndSend(exchange, routingKey, human);
    }
}
