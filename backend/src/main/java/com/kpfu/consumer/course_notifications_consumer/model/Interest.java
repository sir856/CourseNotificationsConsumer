package com.kpfu.consumer.course_notifications_consumer.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Interest {
    @EmbeddedId
    private InterestKey id = new InterestKey();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "knowledge_id")
    @MapsId("knowledgeId")
    private Knowledge knowledge;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tag_id")
    @MapsId("tagId")
    private Tag tag;

    @ManyToMany(mappedBy = "interests", fetch = FetchType.LAZY)
    private Set<User> users;

    public InterestKey getId() {
        return id;
    }

    public Knowledge getKnowledge() {
        return knowledge;
    }

    public Tag getTag() {
        return tag;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interest interest = (Interest) o;
        return id.equals(interest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
