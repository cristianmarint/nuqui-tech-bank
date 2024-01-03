package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Me;
import co.nuqui.tech.msusers.domain.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserEventPublisher.class);

    @Value("${spring.rabbitmq.user.exchange}")
    private String userExchange;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, User user) {
        logger.info("publish event routingKey: {}, user: {}", routingKey, user);
        rabbitTemplate.convertAndSend(userExchange, routingKey, user);
    }

    public void publish(String routingKey, Me me) {
        logger.info("publish event routingKey: {}, me: {}", routingKey, me);
        rabbitTemplate.convertAndSend(userExchange, routingKey, me);
    }
}
