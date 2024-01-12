package co.nuqui.tech.msbatchprocess.app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object object) {
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), object);
        log.info("Kafka Producer topic {} message {}", topic, object);
    }
}
