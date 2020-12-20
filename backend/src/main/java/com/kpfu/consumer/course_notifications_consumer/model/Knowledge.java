package com.kpfu.consumer.course_notifications_consumer.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Knowledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "knowledge", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Interest> interests;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
        Knowledge knowledge = (Knowledge) o;
        return id == knowledge.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
