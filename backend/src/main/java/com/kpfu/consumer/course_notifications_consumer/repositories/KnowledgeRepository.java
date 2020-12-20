package com.kpfu.consumer.course_notifications_consumer.repositories;

import com.kpfu.consumer.course_notifications_consumer.model.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Integer> {
}
