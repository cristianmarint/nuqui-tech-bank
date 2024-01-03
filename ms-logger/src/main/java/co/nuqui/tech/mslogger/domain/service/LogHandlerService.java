package co.nuqui.tech.mslogger.domain.service;

import co.nuqui.tech.msdeposits.domain.dto.Deposit;
import co.nuqui.tech.mshumans.domain.dto.Human;
import co.nuqui.tech.mslogger.domain.dto.Log;
import co.nuqui.tech.mslogger.infrastructure.LogRepository;
import co.nuqui.tech.msusers.domain.dto.Me;
import co.nuqui.tech.msusers.domain.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ReactiveStreamsUnusedPublisher")
@Service
public class LogHandlerService {

    @Autowired
    private LogRepository logRepository;

    private Logger logger = LoggerFactory.getLogger(LogHandlerService.class);

    @Value("${spring.rabbitmq.user.created.routing-key}")
    private String rabbitMQUserCreateKey;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user.queue", durable = "true"),
            exchange = @Exchange(value = "exchange.name", type = ExchangeTypes.TOPIC),
            key = {"user.created", "user.login", "user.logout",
                    "user.deleted", "user.blocked","user.inactive"
            })
    )
    public void rabbitMQListenerHuman(
            @Payload User data,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key
    ) {
        logRepository.save(
                        Log.builder()
                                .id(UUID.randomUUID())
                                .action(key)
                                .data(data)
                                .build()
                ).doOnError(error -> logger.error("Error saving log {}", error.getMessage()))
                .subscribe(log -> logger.info("Save log {} {}",key, log));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user.queue", durable = "true"),
            exchange = @Exchange(value = "exchange.name", type = ExchangeTypes.TOPIC),
            key = {"user.me"})
    )
    public void rabbitMQListenerHuman(
            @Payload Me data,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key
    ) {
        logRepository.save(
                        Log.builder()
                                .id(UUID.randomUUID())
                                .action(key)
                                .data(data)
                                .build()
                ).doOnError(error -> logger.error("Error saving log {}", error.getMessage()))
                .subscribe(log -> logger.info("Save log {} {}",key, log));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "human.queue", durable = "true"),
            exchange = @Exchange(value = "exchange.name", type = ExchangeTypes.TOPIC),
            key = {"human.created","human.search"})
    )
    public void rabbitMQListenerHuman(
            @Payload Human data,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key
    ) {
        logRepository.save(
                        Log.builder()
                                .id(UUID.randomUUID())
                                .action(key)
                                .data(data)
                                .build()
                ).doOnError(error -> logger.error("Error saving log {}", error.getMessage()))
                .subscribe(log -> logger.info("Save log {} {}",key, log));
    }

    @KafkaListener(topics = {"deposits.search"}, groupId = "nuqui.tech")
    public void kafkaListenerSearch(
            @Payload List<Deposit> data,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        logRepository.save(
                        Log.builder()
                                .id(UUID.randomUUID())
                                .action(topic)
                                .data(data)
                                .build()
                ).doOnError(error -> logger.error("Error saving log {}", error.getMessage()))
                .subscribe(log -> logger.info("Save log {} {}",topic, log));
    }

    @KafkaListener(topics = {"deposits.transactions"}, groupId = "nuqui.tech")
    public void kafkaListenerTransactions(
            @Payload List<Deposit> data,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        logRepository.save(
                        Log.builder()
                                .id(UUID.randomUUID())
                                .action(topic)
                                .data(data)
                                .build()
                ).doOnError(error -> logger.error("Error saving log {}", error.getMessage()))
                .subscribe(log -> logger.info("Save log {} {}",topic, log));
    }

    @KafkaListener(topics = {"deposits.transfer"}, groupId = "nuqui.tech")
    public void kafkaListenerTransfer(
            @Payload List<Deposit> data,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        logRepository.save(
                        Log.builder()
                                .id(UUID.randomUUID())
                                .action(topic)
                                .data(data)
                                .build()
                ).doOnError(error -> logger.error("Error saving log {}", error.getMessage()))
                .subscribe(log -> logger.info("Save log {} {}",topic, log));
    }
}
