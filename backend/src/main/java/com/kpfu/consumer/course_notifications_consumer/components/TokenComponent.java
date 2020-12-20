package com.kpfu.consumer.course_notifications_consumer.components;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenComponent {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private Map<String, String> userToken = new HashMap<>();

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public void addUserToken(String id, String token, String session) {
        userToken.put(id, token + session);
    }

    public String getToken(String id) {
        if (!userToken.containsKey(id)) {
            throw new IllegalArgumentException("Wrong id");
        }
        return userToken.get(id);
    }


}
