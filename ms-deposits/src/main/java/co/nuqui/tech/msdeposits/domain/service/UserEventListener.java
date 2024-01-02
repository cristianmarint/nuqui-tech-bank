package co.nuqui.tech.msdeposits.domain.service;

import co.nuqui.tech.msdeposits.domain.dto.User;
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
public class UserEventListener {

    @Autowired
    private final DepositService depositService;

    @Autowired
    private final Jackson2JsonMessageConverter messageConverter;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user.queue", durable = "true"),
            exchange = @Exchange(value = "user.exchange", type = ExchangeTypes.DIRECT),
            key = "user.created")
    )
    public void receiveHumanEvent(@Payload User user) {
        log.info("receive UserCreated Event: {}", user);
        log.info("deposit created : {}", depositService.createDepositForHuman(user).subscribe());
    }
}