package com.kpfu.consumer.course_notifications_consumer.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "user_interests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = {@JoinColumn(name = "knowledge_id"), @JoinColumn(name = "tag_id")}
    )
    private Set<Interest> interests = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserNotification> notifications;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public String getPassword() {
        return password;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<UserNotification> getNotifications() {
        return notifications;
    }
}
