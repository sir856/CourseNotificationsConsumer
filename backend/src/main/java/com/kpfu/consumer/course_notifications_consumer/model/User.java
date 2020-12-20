package com.kpfu.consumer.course_notifications_consumer.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    private String name;

    private String password;

    @ManyToMany
    private List<Interest> interests;

    public String getName() {
        return name;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public String getPassword() {
        return password;
    }
}
