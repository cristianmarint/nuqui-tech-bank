package co.nuqui.tech.mshumans.app.config;

import co.nuqui.tech.mshumans.domain.service.HumanEventPublisher;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.template.exchange}")
    private String RABBIT_EXCHANGE;

    @Value("${spring.rabbitmq.template.default-receive-queue}")
    private String defaultReceiveQueue;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    @Value("${spring.rabbitmq.port}")
    private int RABBITMQ_PORT;

    @Value("${spring.rabbitmq.username}")
    private String RABBITMQ_USERNAME;

    @Value("${spring.rabbitmq.password}")
    private String RABBITMQ_PASSWORD;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(RABBIT_EXCHANGE);
    }

    @Bean
    public HumanEventPublisher humanEventPublisher(RabbitTemplate rabbitTemplate) {
        return new HumanEventPublisher(rabbitTemplate, RABBIT_EXCHANGE, defaultReceiveQueue, routingKey);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(RABBITMQ_HOST);
        connectionFactory.setPort(RABBITMQ_PORT);
        connectionFactory.setUsername(RABBITMQ_USERNAME);
        connectionFactory.setPassword(RABBITMQ_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareExchange(exchange());
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
