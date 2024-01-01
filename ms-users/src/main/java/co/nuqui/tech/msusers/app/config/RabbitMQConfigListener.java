package co.nuqui.tech.msusers.app.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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

    @Value("${spring.rabbitmq.user.created.routing-key}")
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
    DirectExchange humanExchange() {
        return new DirectExchange(humanExchange);
    }

    @Bean
    DirectExchange humanDeadLetterExchange() {
        return new DirectExchange(DLE);
    }

    @Bean
    Queue humanQueue() {
        return new Queue(humanQueue);
    }

    @Bean
    Binding bindingUsers() {
        return BindingBuilder
                .bind(humanQueue())
                .to(humanExchange())
                .with(humanRoutingKey);
    }
}