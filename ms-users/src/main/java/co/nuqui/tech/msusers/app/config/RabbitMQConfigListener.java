package co.nuqui.tech.msusers.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfigListener {

    @Value("${spring.rabbitmq.user.exchange}")
    private String humanExchange;

    @Value("${spring.rabbitmq.user.default-receive-queue}")
    private String humanQueue;

    @Value("${spring.rabbitmq.user.routing-key}")
    private String humanRoutingKey;

    public static final String DLE = "human.exchange.dle";
    public static final String DLQ = "human.queue.dlq";
    public static final String ROUTING_KEY_DLQ = "register.human.dlq";


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean("rabbitListenerContainerFactory")
    public RabbitListenerContainerFactory<?> rabbitFactory(
            @Autowired ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    TopicExchange humanExchange() {
        return new TopicExchange(humanExchange);
    }

    @Bean
    DirectExchange humanDeadLetterExchange() {
        return new DirectExchange(DLE);
    }

    @Bean
    Queue humanQueue() {
        return QueueBuilder.durable(humanQueue)
                .withArgument(
                        "x-dead-letter-exchange",
                        DLE
                )
                .withArgument(
                        "x-dead-letter-routing-key",
                        ROUTING_KEY_DLQ
                )
                .build();
    }

    @Bean
    Queue humanDeadLetterQueue() {
        return QueueBuilder.durable(DLQ)
                .build();
    }

    @Bean
    Binding bindingUsers() {
        return BindingBuilder
                .bind(humanQueue())
                .to(humanExchange())
                .with(humanRoutingKey);
    }

    @Bean
    Binding bindingDLQUsers() {
        return BindingBuilder
                .bind(humanDeadLetterQueue())
                .to(humanDeadLetterExchange())
                .with(ROUTING_KEY_DLQ);
    }
}