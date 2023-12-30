package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.user.exchange}")
    private String userExchange;

    @Value("${spring.rabbitmq.user.default-receive-queue}")
    private String userQueue;

    @Value("${spring.rabbitmq.user.routing-key}")
    private String userRoutingKey;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(User user) {
        rabbitTemplate.convertAndSend(userExchange, userRoutingKey, user);
    }
}
