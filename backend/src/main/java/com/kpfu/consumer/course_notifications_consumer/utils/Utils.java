package com.kpfu.consumer.course_notifications_consumer.utils;

import com.kpfu.consumer.course_notifications_consumer.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.management.Notification;
import java.util.*;

public class Utils {
    public static String getSessionFromCookies(String cookies) {
        String[] cookiesArray = cookies.split(";");

        for (String cookie : cookiesArray) {
            if (cookie.startsWith("sessionId")) {
                return cookie.split("=")[1];
            }
        }

        return null;

    }

    public static JSONObject getUserJson(User user) {
        JSONObject userJson = new JSONObject();
        JSONArray interestsJsonArray = new JSONArray();

        userJson.put("id", user.getId()).put("name", user.getName());

        Map<Knowledge, Set<Tag>> interestsMap = getInterestsMap(user);

        for (Knowledge knowledge : interestsMap.keySet()) {
            JSONObject knowledgeJson = new JSONObject();
            JSONArray tagsJsonArray = new JSONArray();

            knowledgeJson.put("id", knowledge.getId()).put("name", knowledge.getName());

            for (Tag tag : interestsMap.get(knowledge)) {
                JSONObject tagJson = new JSONObject();
                tagJson.put("id", tag.getId());
                tagJson.put("name", tag.getName());
                tagsJsonArray.put(tagJson);
            }

            knowledgeJson.put("tags", tagsJsonArray);
            interestsJsonArray.put(knowledgeJson);
        }

        userJson.put("interests", interestsJsonArray);

        userJson.put("notifications", getUserNotificationsJson(user.getNotifications()));

        return userJson;
    }

    public static JSONObject getUserNotificationJson(UserNotification userNotification) {
        JSONObject userNotificationJson = new JSONObject();

        userNotificationJson.put("id", userNotification.getId());
        userNotificationJson.put("message", userNotification.getMessage());
        userNotificationJson.put("user", getUserJson(userNotification.getUser()));

        return userNotificationJson;
    }

    private static JSONArray getUserNotificationsJson(Set<UserNotification> notifications) {
        JSONArray notificationsJsonArray = new JSONArray();

        for (UserNotification notification : notifications) {
            JSONObject userNotificationJson = new JSONObject();

            userNotificationJson.put("id", notification.getId());
            userNotificationJson.put("message", new JSONObject(notification.getMessage()));

            notificationsJsonArray.put(userNotificationJson);
        }


        return notificationsJsonArray;
    }

    public static JSONObject getUserSubscriptionsJson(User user) {
        JSONObject userJson = new JSONObject();
        JSONArray interestsJsonArray = new JSONArray();

        userJson.put("userId", user.getId());

        Map<Knowledge, Set<Tag>> interestsMap = getInterestsMap(user);

        for (Knowledge knowledge : interestsMap.keySet()) {
            JSONObject knowledgeJson = new JSONObject();
            JSONArray tagsJsonArray = new JSONArray();

            knowledgeJson.put("knowledge", knowledge.getId());

            for (Tag tag : interestsMap.get(knowledge)) {
                tagsJsonArray.put(tag.getId());
            }

            knowledgeJson.put("tags", tagsJsonArray);
            interestsJsonArray.put(knowledgeJson);
        }

        userJson.put("tags", interestsJsonArray);

        return userJson;


    }

    private static Map<Knowledge, Set<Tag>> getInterestsMap(User user) {
        Map<Knowledge, Set<Tag>> interestsMap = new HashMap<>();

        for (Interest interest : user.getInterests()) {
            if (!interestsMap.containsKey(interest.getKnowledge())) {
                interestsMap.put(interest.getKnowledge(), new HashSet<>());
            }

            interestsMap.get(interest.getKnowledge()).add(interest.getTag());
        }

        return interestsMap;
    }
}
