package co.nuqui.tech.msusers.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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

    @Bean
    public Queue humanQueue() {
        return new Queue(humanQueue);
    }

    @Bean
    public Exchange humanExchange() {
        return ExchangeBuilder.directExchange(humanExchange).durable(true).build();
    }

    @Bean
    public Binding humanBinding(Queue humanQueue, Exchange humanExchange) {
        return BindingBuilder.bind(humanQueue).to(humanExchange).with(humanRoutingKey).noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}