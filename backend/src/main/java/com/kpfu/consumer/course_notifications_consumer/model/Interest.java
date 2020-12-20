package com.kpfu.consumer.course_notifications_consumer.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "interest")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String knowledge;

    private String tag;

    @ManyToMany
    private
    List<User> users;

    public int getId() {
        return id;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public String getTag() {
        return tag;
    }

    public List<User> getUsers() {
        return users;
    }
}
