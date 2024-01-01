package co.nuqui.tech.msusers.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfigPublisher {

    @Value("${spring.rabbitmq.user.exchange}")
    private String userExchange;

    @Value("${spring.rabbitmq.user.default-receive-queue}")
    private String userQueue;

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(userExchange);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(userQueue);
    }

    @Value("${spring.rabbitmq.user.login.routing-key}")
    private String userLoginRoutingKey;

    @Value("${spring.rabbitmq.user.logout.routing-key}")
    private String userLogoutRoutingKey;

    @Value("${spring.rabbitmq.user.deleted.routing-key}")
    private String userDeletedRoutingKey;

    @Value("${spring.rabbitmq.user.inactive.routing-key}")
    private String userInactiveRoutingKey;

    @Value("${spring.rabbitmq.user.active.routing-key}")
    private String userActiveRoutingKey;

    @Value("${spring.rabbitmq.user.me.routing-key}")
    private String userMeRoutingKey;

    @Bean
    public Binding bindingUserLoginQueueToExchange(
            @Autowired Queue userQueue,
            @Autowired Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(userLoginRoutingKey).noargs();
    }

    @Bean
    public Binding bindingUserLogoutQueueToExchange(
            @Autowired Queue userQueue,
            @Autowired Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(userLogoutRoutingKey).noargs();
    }

    @Bean
    public Binding bindingUserDeletedQueueToExchange(
            @Autowired Queue userQueue,
            @Autowired Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(userDeletedRoutingKey).noargs();
    }

    @Bean
    public Binding bindingUserActiveQueueToExchange(
            @Autowired Queue userQueue,
            @Autowired Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(userActiveRoutingKey).noargs();
    }

    @Bean
    public Binding bindingUserInactiveQueueToExchange(
            @Autowired Queue userQueue,
            @Autowired Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(userInactiveRoutingKey).noargs();
    }

    @Bean
    public Binding bindingUserMeQueueToExchange(
            @Autowired Queue userQueue,
            @Autowired Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(userMeRoutingKey).noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            @Autowired ConnectionFactory connectionFactory
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean("rabbitAdmin")
    public RabbitAdmin rabbitAdmin(
            @Autowired ConnectionFactory connectionFactory
    ) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareExchange(userExchange());
        rabbitAdmin.declareQueue(userQueue());
        return rabbitAdmin;
    }
}
