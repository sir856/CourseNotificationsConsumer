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
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UsersRepository usersRepository;
    private final TokenComponent tokenComponent;

    @Autowired
    public UserController(TokenComponent tokenComponent, UsersRepository usersRepository) {
        this.tokenComponent = tokenComponent;
        this.usersRepository = usersRepository;
    }


    @GetMapping("/info/{id}")
    public User getInfo(@PathVariable("id") String id, @RequestHeader("token") String token, @RequestHeader("cookie") String cookies) {
        String session = Utils.getSessionFromCookies(cookies);
        if (tokenComponent.getToken(id).equals(token + session)) {
            return usersRepository.findById(id).get();
        }

        throw new IllegalArgumentException("Wrong token");
    }
}
