package com.kpfu.consumer.course_notifications_consumer.repositories;

import com.kpfu.consumer.course_notifications_consumer.model.Interest;
import com.kpfu.consumer.course_notifications_consumer.model.InterestKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestsRepository extends JpaRepository<Interest, InterestKey> {
}
