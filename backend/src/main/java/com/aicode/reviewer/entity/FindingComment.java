package com.aicode.reviewer.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "finding_comments")
public class FindingComment {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finding_id", nullable = false)
    private ReviewFinding finding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FindingComment() {}

    public FindingComment(String id, ReviewFinding finding, User user, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.finding = finding;
        this.user = user;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private ReviewFinding finding;
        private User user;
        private String comment;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder finding(ReviewFinding finding) { this.finding = finding; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder comment(String comment) { this.comment = comment; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public FindingComment build() { return new FindingComment(id, finding, user, comment, createdAt); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ReviewFinding getFinding() { return finding; }
    public void setFinding(ReviewFinding finding) { this.finding = finding; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
