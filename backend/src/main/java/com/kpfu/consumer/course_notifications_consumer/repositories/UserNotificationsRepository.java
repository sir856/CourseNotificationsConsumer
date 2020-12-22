package com.kpfu.consumer.course_notifications_consumer.repositories;

import com.kpfu.consumer.course_notifications_consumer.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationsRepository extends JpaRepository<UserNotification, String> {
}
