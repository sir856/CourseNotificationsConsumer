package com.kpfu.consumer.course_notifications_consumer.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Interest> interests;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
