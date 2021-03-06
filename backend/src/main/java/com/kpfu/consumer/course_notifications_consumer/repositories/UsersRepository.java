package com.kpfu.consumer.course_notifications_consumer.repositories;

import com.kpfu.consumer.course_notifications_consumer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
}
