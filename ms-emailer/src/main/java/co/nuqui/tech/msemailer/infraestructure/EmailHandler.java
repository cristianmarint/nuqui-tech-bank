package co.nuqui.tech.msemailer.infraestructure;

import co.nuqui.tech.msemailer.domain.service.EmailService;
import co.nuqui.tech.mshumans.domain.dto.Human;
import co.nuqui.tech.msusers.domain.dto.Me;
import co.nuqui.tech.msusers.domain.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@SuppressWarnings("ReactiveStreamsUnusedPublisher")
@Service
public class EmailHandler {

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(EmailHandler.class);

    @Value("${mailtrap.email-sender.default-sender-email-address}")
    private String senderEmail;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user.queue", durable = "true"),
            exchange = @Exchange(value = "user.exchange"),
            key = "user.me")
    )
    public void rabbitMQListenerUserMe(
            @Payload Me payload,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key
    ) {
        logger.info("Received message from queue {} {}", key, payload.toString());
        emailService.sendSimpleMessage(payload.getUser().getEmail(), senderEmail, key, payload.toString());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user.queue", durable = "true"),
            exchange = @Exchange(value = "user.exchange"),
            key = {"user.created", "user.login", "user.logout",
                    "user.deleted", "user.blocked", "user.active",
                    "user.inactive"
            })
    )
    public void rabbitMQListenerUser(
            @Payload User payload,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key
    ) {
        logger.info("Received message from queue {} {}", key, payload.toString());
        emailService.sendSimpleMessage(payload.getEmail(), senderEmail, key, payload.toString());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "human.queue", durable = "true"),
            exchange = @Exchange(value = "human.exchange"),
            key = {"human.created", "human.search"})
    )
    public void rabbitMQListenerHuman(
            @Payload Human payload,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key
    ) {
        logger.info("Received message from queue {} {}", key, payload.toString());
        emailService.sendSimpleMessage(payload.getEmail(), senderEmail, key, payload.toString());
    }
}
