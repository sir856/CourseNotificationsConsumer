package com.kpfu.consumer.course_notifications_consumer.controllers;

import com.kpfu.consumer.course_notifications_consumer.components.TokenComponent;
import com.kpfu.consumer.course_notifications_consumer.model.User;
import com.kpfu.consumer.course_notifications_consumer.repositories.UsersRepository;
import com.kpfu.consumer.course_notifications_consumer.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UsersRepository usersRepository;
    private final TokenComponent tokenComponent;


    @Autowired
    public LoginController(UsersRepository usersRepository, TokenComponent tokenComponent) {
        this.usersRepository = usersRepository;
        this.tokenComponent = tokenComponent;
    }

    @PostMapping
    public User register(@RequestBody User user) {
        if (usersRepository.findById(user.getName()).isPresent()) {
            throw new IllegalArgumentException("User with name: " + user.getName() + " already exists");
        }
        return usersRepository.save(user);
    }

    @PutMapping
    public String login(@RequestBody User data, @RequestHeader("cookie") String cookies) {
        String session = Utils.getSessionFromCookies(cookies);

        User user = usersRepository.findById(data.getName()).orElseThrow(() -> new IllegalArgumentException("Wrong name"));

        if (user.getPassword().equals(data.getPassword())) {
            String token = TokenComponent.generateNewToken();
            tokenComponent.addUserToken(user.getName(), token, session);
            return token;
        }

        throw new IllegalArgumentException("Wrong password");
    }
}
