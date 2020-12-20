package com.kpfu.consumer.course_notifications_consumer.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InterestKey implements Serializable {
    @Column(name = "knowledge_id")
    private Integer knowledgeId;

    @Column(name = "tag_id")
    private Integer tagId;

    public Integer getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Integer knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterestKey that = (InterestKey) o;
        return knowledgeId.equals(that.knowledgeId) &&
                tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(knowledgeId, tagId);
    }
}
