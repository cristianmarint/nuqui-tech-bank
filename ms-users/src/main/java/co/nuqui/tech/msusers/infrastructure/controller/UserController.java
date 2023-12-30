package co.nuqui.tech.msusers.infrastructure.controller;

import co.nuqui.tech.msusers.domain.dto.Me;
import co.nuqui.tech.msusers.domain.dto.User;
import co.nuqui.tech.msusers.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.nuqui.tech.msusers.infrastructure.controller.Mappings.URL_USERS_AUTH_V1;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = URL_USERS_AUTH_V1)
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<User> login(@RequestBody User user) {
        return ResponseEntity.status(OK).body(userService.login(user));
    }


    @PostMapping("logout")
    public ResponseEntity<User> logout(@RequestBody User user) {
        return ResponseEntity.status(OK).body(userService.logout(user));
    }

    @PostMapping("delete")
    public ResponseEntity<User> delete(@RequestBody User user) {
        return ResponseEntity.status(OK).body(userService.delete(user));
    }

    @PostMapping("me")
    public ResponseEntity<Me> me(@RequestBody User user) {
        return ResponseEntity.status(OK).body(userService.me(user));
    }

}