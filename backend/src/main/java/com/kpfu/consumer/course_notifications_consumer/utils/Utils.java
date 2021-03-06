package com.kpfu.consumer.course_notifications_consumer.utils;

import com.kpfu.consumer.course_notifications_consumer.model.Interest;
import com.kpfu.consumer.course_notifications_consumer.model.Knowledge;
import com.kpfu.consumer.course_notifications_consumer.model.Tag;
import com.kpfu.consumer.course_notifications_consumer.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utils {
    public static String getSessionFromCookies(String cookies) {
        String[] cookiesArray = cookies.split(";");

        for (String cookie : cookiesArray) {
            if (cookie.startsWith("JSESSIONID")) {
                return cookie.split("=")[1];
            }
        }

        return null;

    }

    public static JSONObject getUserJson(User user) {
        JSONObject userJson = new JSONObject();

        userJson.put("id", user.getId());
        userJson.put("name", user.getName());

        JSONArray interestsJsonArray = new JSONArray();

        Map<Knowledge, Set<Tag>> interestsMap = new HashMap<>();

        for (Interest interest : user.getInterests()) {
            if (!interestsMap.containsKey(interest.getKnowledge())) {
                interestsMap.put(interest.getKnowledge(), new HashSet<>());
            }

            interestsMap.get(interest.getKnowledge()).add(interest.getTag());
        }

        for (Knowledge knowledge : interestsMap.keySet()) {
            JSONObject knowledgeJson = new JSONObject();
            JSONArray tagsJsonArray = new JSONArray();

            knowledgeJson.put("id", knowledge.getId());
            knowledgeJson.put("name", knowledge.getName());

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

        return userJson;
    }
}
