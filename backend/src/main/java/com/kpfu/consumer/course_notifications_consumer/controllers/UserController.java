package com.kpfu.consumer.course_notifications_consumer.controllers;

import com.kpfu.consumer.course_notifications_consumer.components.TokenComponent;
import com.kpfu.consumer.course_notifications_consumer.model.*;
import com.kpfu.consumer.course_notifications_consumer.repositories.InterestsRepository;
import com.kpfu.consumer.course_notifications_consumer.repositories.UsersRepository;
import com.kpfu.consumer.course_notifications_consumer.utils.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;


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


    @GetMapping(value = "/info/{id}", produces = "application/json")
    public String getInfo(@PathVariable("id") int id, @RequestHeader("token") String token, @RequestHeader("cookie") String cookies) {
        String session = Utils.getSessionFromCookies(cookies);
        if (tokenComponent.getToken(id).equals(token + session)) {
            User user = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Wrong user id"));

            return Utils.getUserJson(user).toString();
        }

        throw new IllegalArgumentException("Wrong token");
    }

    @PutMapping(value = "/interests/add/{id}", produces = "application/json")
    public String setInterests(@PathVariable("id") int id,
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

            usersRepository.save(user);

            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.put("Content-Type", Arrays.asList("application/json"));
            HttpEntity<String>  request = new HttpEntity<>(Utils.getUserSubscriptionsJson(user).toString(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity("http://10.160.178.168/subscriptions/subscript", request, String.class);
            logger.info("response code: " +  response.getStatusCodeValue());
            logger.info(response.getBody());
            return Utils.getUserJson(user).toString();
        }

        throw new IllegalArgumentException("Wrong token");
    }
}
