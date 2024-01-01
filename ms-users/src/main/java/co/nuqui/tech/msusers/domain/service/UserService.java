package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Human;
import co.nuqui.tech.msusers.domain.dto.Me;
import co.nuqui.tech.msusers.domain.dto.User;
import co.nuqui.tech.msusers.infrastructure.controller.GlobalException;
import co.nuqui.tech.msusers.infrastructure.persistence.UserRepository;
import co.nuqui.tech.msusers.infrastructure.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Value("${spring.rabbitmq.user.created.routing-key}")
    private String userCreatedRoutingKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HumanService humanService;

    @Autowired
    private UserEventPublisher userEventPublisher;


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

    @Autowired
    private JwtProvider jwtProvider;

    public void create(Human human) {
        User user = User.builder()
                .humanId(human.getId())
                .email(human.getEmail())
                .username(human.getUsername())
                .password(human.getPassword())
                .build();

        if (userRepository.findByEmailIgnoreCase(user.getEmail()) == null &&
                userRepository.findByUsernameIgnoreCase(user.getUsername()) == null) {
            user.setRecentActivity("CREATED SUCCESSFUL AT " + Instant.now().toString());
            user.setStatus("ACTIVE");
            userRepository.save(user);
            userEventPublisher.publish(userCreatedRoutingKey, user);
            logger.info("created user for human: {}", human);
            return;
        }
        throw new GlobalException("user already exists " + user.getEmail());
    }


    public User login(User user) {
        user.setToken(jwtProvider.generateToken(user));
        user.setRecentActivity("LOGIN SUCCESSFUL AT " + Instant.now().toString());
        userRepository.save(user);
        logger.info("login user: {}", user);
        userEventPublisher.publish(userLoginRoutingKey, user);
        return user;
    }

    public User logout(User user) {
        user.setRecentActivity("LOGOUT SUCCESSFUL AT " + Instant.now().toString());
        user.setToken(null);
        userRepository.save(user);
        userEventPublisher.publish(userLogoutRoutingKey, user);
        logger.info("logout user: {}", user);
        return user;
    }

    public User delete(User user) {
        user.setRecentActivity("DELETED SUCCESSFUL AT " + Instant.now().toString());
        user.setStatus("DELETED");
        user.setDeletedAt(Instant.now().toString());
        userRepository.save(user);
        userEventPublisher.publish(userDeletedRoutingKey, user);
        logger.info("deleted user: {}", user);
        return user;
    }

    public User active(User user) {
        user.setRecentActivity("ACTIVE SUCCESSFUL AT " + Instant.now().toString());
        user.setStatus("ACTIVE");
        user.setDeletedAt(Instant.now().toString());
        userRepository.save(user);
        userEventPublisher.publish(userActiveRoutingKey, user);
        logger.info("active user: {}", user);
        return user;
    }

    public User inactive(User user) {
        user.setRecentActivity("INACTIVE SUCCESSFUL AT " + Instant.now().toString());
        user.setStatus("INACTIVE");
        user.setDeletedAt(Instant.now().toString());
        userRepository.save(user);
        userEventPublisher.publish(userInactiveRoutingKey, user);
        logger.info("inactive user: {}", user);
        return user;
    }

    public Me me(User user) {
        User byUsernameIgnoreCase = userRepository.findByUsernameIgnoreCase(user.getUsername());
        Human human = humanService.findByIdentification(byUsernameIgnoreCase.getHumanId());
//        Deposits deposits = depositsService.findByIdentification(byUsernameIgnoreCase.getHumanId());
        userEventPublisher.publish(userMeRoutingKey, user);
        logger.info("me user: {}", user);
        return Me.builder()
                .human(human)
                .user(byUsernameIgnoreCase)
                .build();
    }
}