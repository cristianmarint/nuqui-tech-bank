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
import java.util.Objects;

import static co.nuqui.tech.msusers.domain.dto.UserStatusConstants.*;


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
    private DepositService depositService;

    @Autowired
    private UserEventPublisher userEventPublisher;


    @Value("${spring.rabbitmq.user.login.routing-key}")
    private String userLoginRoutingKey;

    @Value("${spring.rabbitmq.user.logout.routing-key}")
    private String userLogoutRoutingKey;

    @Value("${spring.rabbitmq.user.deleted.routing-key}")
    private String userDeletedRoutingKey;

    @Value("${spring.rabbitmq.user.blocked.routing-key}")
    private String userBlockedRoutingKey;

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

        if (userRepository.findByEmail(user.getEmail()) == null &&
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
        User currentRecord = userRepository.findByEmail(user.getEmail());
        logger.info("login currentRecord: {}", currentRecord);

        Objects.requireNonNull(currentRecord, "USER NOT FOUND " + user.getEmail());
        if (!currentRecord.getPassword().equals(user.getPassword())) throw new GlobalException("INVALID PASSWORD");
        if (currentRecord.getStatus().equals(BLOCKED))
            throw new GlobalException("USER ACCOUNTS CANNOT BE ACCESS " + BLOCKED + " " + user.getEmail());
        if (currentRecord.getStatus().equals(INACTIVE))
            throw new GlobalException("USER ACCOUNTS CANNOT BE ACCESS " + INACTIVE + " " + user.getEmail());
        if (currentRecord.getStatus().equals(DELETED))
            throw new GlobalException("USER ACCOUNTS CANNOT BE ACCESS " + DELETED + " " + user.getEmail());

        currentRecord.setToken(jwtProvider.generateToken(user));
        currentRecord.setRecentActivity("LOGIN SUCCESSFUL AT " + Instant.now().toString());
        userRepository.update(currentRecord);
        logger.info("login user: {}", currentRecord);
        userEventPublisher.publish(userLoginRoutingKey, currentRecord);
        return currentRecord;
    }

    public User logout(User user) {
        User currentRecord = userRepository.findByEmail(user.getEmail());
        currentRecord.setRecentActivity("LOGOUT SUCCESSFUL AT " + Instant.now().toString());
        currentRecord.setToken(null);

        userRepository.update(currentRecord);
        userEventPublisher.publish(userLogoutRoutingKey, currentRecord);
        logger.info("logout user: {}", currentRecord);
        return currentRecord;
    }

    public User delete(User user) {
        User currentRecord = userRepository.findByEmail(user.getEmail());
        currentRecord.setRecentActivity("DELETED SUCCESSFUL AT " + Instant.now().toString());
        currentRecord.setStatus(DELETED);
        currentRecord.setDeletedAt(Instant.now().toString());
        userRepository.update(user);
        userEventPublisher.publish(userDeletedRoutingKey, currentRecord);
        logger.info("deleted user: {}", currentRecord);
        return currentRecord;
    }

    public User active(User user) {
        User currentRecord = userRepository.findByEmail(user.getEmail());
        currentRecord.setRecentActivity("ACTIVE SUCCESSFUL AT " + Instant.now().toString());
        currentRecord.setStatus(ACTIVE);
        currentRecord.setDeletedAt(Instant.now().toString());
        userRepository.update(currentRecord);
        userEventPublisher.publish(userActiveRoutingKey, currentRecord);
        logger.info("active user: {}", currentRecord);
        return currentRecord;
    }

    public User inactive(User user) {
        User currentRecord = userRepository.findByEmail(user.getEmail());
        currentRecord.setRecentActivity("INACTIVE SUCCESSFUL AT " + Instant.now().toString());
        currentRecord.setStatus(INACTIVE);
        currentRecord.setDeletedAt(Instant.now().toString());
        userRepository.update(currentRecord);
        userEventPublisher.publish(userInactiveRoutingKey, currentRecord);
        logger.info("inactive user: {}", currentRecord);
        return currentRecord;
    }

    public User blocked(User user) {
        User currentRecord = userRepository.findByEmail(user.getEmail());
        currentRecord.setRecentActivity("BLOCKED SUCCESSFUL AT " + Instant.now().toString());
        currentRecord.setStatus(BLOCKED);
        currentRecord.setDeletedAt(Instant.now().toString());
        userRepository.update(currentRecord);
        userEventPublisher.publish(userBlockedRoutingKey, currentRecord);
        logger.info("inactive user: {}", currentRecord);
        return currentRecord;
    }

    public Me me(User user) {
        User currentRecord = userRepository.findByEmail(user.getEmail());
        logger.info("user user: {}", currentRecord);

        Me me = Me.builder()
                .human(humanService.findById(currentRecord.getHumanId()))
                .user(currentRecord)
                .deposits(depositService.findByHumanId(currentRecord.getHumanId()))
                .build();
        logger.info("me user: {}", me);
        userEventPublisher.publish(userMeRoutingKey, me);
        return me;
    }
}