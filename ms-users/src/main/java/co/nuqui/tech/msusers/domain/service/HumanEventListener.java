package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Human;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class HumanEventListener {

    @Autowired
    private final UserService userService;

    @Autowired
    private final Jackson2JsonMessageConverter messageConverter;

    @RabbitListener(queues = "human.queue")
    public void receiveHumanEvent(@Payload Human human) {
        userService.create(human);
    }
}