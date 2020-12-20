package com.kpfu.consumer.course_notifications_consumer.utils;

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
}
