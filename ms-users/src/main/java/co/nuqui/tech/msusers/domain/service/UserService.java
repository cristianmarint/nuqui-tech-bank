package co.nuqui.tech.msusers.domain.service;

import co.nuqui.tech.msusers.domain.dto.Human;
import co.nuqui.tech.msusers.domain.dto.Me;
import co.nuqui.tech.msusers.domain.dto.User;
import co.nuqui.tech.msusers.infrastructure.controller.GlobalException;
import co.nuqui.tech.msusers.infrastructure.persistence.UserRepository;
import co.nuqui.tech.msusers.infrastructure.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final HumanService humanService;
    @Autowired
    private UserEventPublisher userEventPublisher;

    private final JwtProvider jwtProvider;

    public void create(Human human) {
        User user = User.builder()
                .humanId(human.getId())
                .email(human.getEmail())
                .username(human.getUsername())
                .password(human.getPassword())
                .build();

        if (userRepository.findByEmailIgnoreCase(user.getEmail()) == null &&
                userRepository.findByUsernameIgnoreCase(user.getUsername()) == null) {
            userRepository.save(user);
            userEventPublisher.publish(user);
            return;
        }
        throw new GlobalException("user already exists " + user.getEmail());
    }


    public User login(User user) {
        String token = jwtProvider.generateToken(user);
        user.setToken(token);
        user.setStatus("LOGIN SUCCESSFUL");
        userRepository.save(user);
        return user;
    }

    public User logout(User user) {
        user.setStatus("LOGOUT SUCCESSFUL");
        user.setToken(null);
        userRepository.save(user);
        return user;
    }

    public User delete(User user) {
        user.setStatus("DELETED SUCCESSFUL");
        user.setDeletedAt(Instant.now().toString());
        userRepository.save(user);
        return user;
    }

    public Me me(User user) {
        User byUsernameIgnoreCase = userRepository.findByUsernameIgnoreCase(user.getUsername());
        Human human = humanService.findByIdentification(byUsernameIgnoreCase.getHumanId());
        return Me.builder()
                .human(human)
                .user(byUsernameIgnoreCase)
                .build();
    }
}