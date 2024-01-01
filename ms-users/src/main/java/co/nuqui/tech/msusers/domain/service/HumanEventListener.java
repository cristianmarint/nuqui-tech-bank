package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Human;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class HumanEventListener {

    @Autowired
    private final UserService userService;

    @Autowired
    private final Jackson2JsonMessageConverter messageConverter;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "human.queue", durable = "true"),
            exchange = @Exchange(value = "human.exchange", type = ExchangeTypes.DIRECT),
            key = "human.created")
    )
    public void receiveHumanEvent(@Payload Human human) {
        log.info("receive HumanCreated Event: {}", human);
        userService.create(human);
    }
}