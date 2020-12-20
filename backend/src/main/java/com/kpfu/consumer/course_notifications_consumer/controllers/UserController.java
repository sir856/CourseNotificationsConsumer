package com.kpfu.consumer.course_notifications_consumer.controllers;

import com.kpfu.consumer.course_notifications_consumer.components.TokenComponent;
import com.kpfu.consumer.course_notifications_consumer.model.Interest;
import com.kpfu.consumer.course_notifications_consumer.model.InterestKey;
import com.kpfu.consumer.course_notifications_consumer.model.User;
import com.kpfu.consumer.course_notifications_consumer.repositories.InterestsRepository;
import com.kpfu.consumer.course_notifications_consumer.repositories.UsersRepository;
import com.kpfu.consumer.course_notifications_consumer.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UsersRepository usersRepository;
    private final TokenComponent tokenComponent;
    private final InterestsRepository interestsRepository;

    @Autowired
    public UserController(TokenComponent tokenComponent, UsersRepository usersRepository, InterestsRepository interestsRepository) {
        this.tokenComponent = tokenComponent;
        this.usersRepository = usersRepository;
        this.interestsRepository = interestsRepository;
    }


    @GetMapping("/info/{id}")
    public User getInfo(@PathVariable("id") int id, @RequestHeader("token") String token, @RequestHeader("cookie") String cookies) {
        String session = Utils.getSessionFromCookies(cookies);
        if (tokenComponent.getToken(id).equals(token + session)) {
            return usersRepository.findById(id).orElse(null);
        }

        throw new IllegalArgumentException("Wrong token");
    }

    @PutMapping("/interests/add/{id}")
    public User setInterests(@PathVariable("id") int id,
                             @RequestHeader("token") String token,
                             @RequestHeader("cookie") String cookies,
                             @RequestBody Set<InterestKey> interestsId) {
        String session = Utils.getSessionFromCookies(cookies);

        if (tokenComponent.getToken(id).equals(token + session)) {
            User user = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Wrong user id"));
            Set<Interest> interests = new HashSet<>();
            for (InterestKey interestId : interestsId) {
                Interest interest = interestsRepository.findById(interestId)
                        .orElseThrow(() -> new IllegalArgumentException("Wrong interest id"));
                interests.add(interest);
            }

            user.setInterests(interests);

            return usersRepository.save(user);
        }

        throw new IllegalArgumentException("Wrong token");
    }
}
