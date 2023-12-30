package co.nuqui.tech.mshumans.app.config;

import co.nuqui.tech.mshumans.domain.service.HumanEventPublisher;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.human.exchange}")
    private String humanExchange;

    @Value("${spring.rabbitmq.human.default-receive-queue}")
    private String humanQueue;

    @Value("${spring.rabbitmq.human.routing-key}")
    private String humanRoutingKey;

    @Value("${spring.rabbitmq.user.exchange}")
    private String userExchange;

    @Value("${spring.rabbitmq.user.default-receive-queue}")
    private String userQueue;

    @Value("${spring.rabbitmq.user.routing-key}")
    private String userRoutingKey;

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    @Value("${spring.rabbitmq.port}")
    private int RABBITMQ_PORT;

    @Value("${spring.rabbitmq.username}")
    private String RABBITMQ_USERNAME;

    @Value("${spring.rabbitmq.password}")
    private String RABBITMQ_PASSWORD;

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(userExchange);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(userQueue);
    }

    @Bean
    public DirectExchange humanExchange() {
        return new DirectExchange(humanExchange);
    }

    @Bean
    public Queue humanQueue() {
        return new Queue(humanQueue);
    }

    @Bean
    public Binding bindingHumanQueueToExchange(
            @Autowired Queue humanQueue,
            @Autowired Exchange humanExchange) {
        return BindingBuilder.bind(humanQueue).to(humanExchange).with(humanRoutingKey).noargs();
    }

    @Bean
    public Binding bindingUserQueueToExchange(
            @Autowired Queue userQueue,
            @Autowired Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(humanRoutingKey).noargs();
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
        rabbitAdmin.declareExchange(humanExchange());
        rabbitAdmin.declareExchange(userExchange());

        rabbitAdmin.declareQueue(humanQueue());
        rabbitAdmin.declareQueue(userQueue());

        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
