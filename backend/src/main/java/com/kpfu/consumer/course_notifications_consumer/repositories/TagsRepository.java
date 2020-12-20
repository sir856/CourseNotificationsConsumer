package com.kpfu.consumer.course_notifications_consumer.repositories;

import com.kpfu.consumer.course_notifications_consumer.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tag, Integer> {
}
