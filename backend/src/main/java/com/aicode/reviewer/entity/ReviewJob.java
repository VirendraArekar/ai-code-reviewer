package com.aicode.reviewer.entity;

import com.aicode.reviewer.entity.enums.ReviewJobStatus;
import com.aicode.reviewer.entity.enums.ReviewMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_jobs")
public class ReviewJob {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id", nullable = false)
    private PullRequest pullRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ReviewJobStatus status = ReviewJobStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_mode", nullable = false, length = 50)
    private ReviewMode reviewMode = ReviewMode.STANDARD;

    @Column(name = "duration_ms")
    private Long durationMs = 0L;

    @Column(name = "token_count")
    private Integer tokenCount = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ReviewJob() {}

    public ReviewJob(String id, PullRequest pullRequest, ReviewJobStatus status, ReviewMode reviewMode,
                     Long durationMs, Integer tokenCount, String errorMessage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.pullRequest = pullRequest;
        this.status = status != null ? status : ReviewJobStatus.PENDING;
        this.reviewMode = reviewMode != null ? reviewMode : ReviewMode.STANDARD;
        this.durationMs = durationMs != null ? durationMs : 0L;
        this.tokenCount = tokenCount != null ? tokenCount : 0;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private PullRequest pullRequest;
        private ReviewJobStatus status = ReviewJobStatus.PENDING;
        private ReviewMode reviewMode = ReviewMode.STANDARD;
        private Long durationMs = 0L;
        private Integer tokenCount = 0;
        private String errorMessage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder pullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; return this; }
        public Builder status(ReviewJobStatus status) { this.status = status; return this; }
        public Builder reviewMode(ReviewMode reviewMode) { this.reviewMode = reviewMode; return this; }
        public Builder durationMs(Long durationMs) { this.durationMs = durationMs; return this; }
        public Builder tokenCount(Integer tokenCount) { this.tokenCount = tokenCount; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ReviewJob build() {
            return new ReviewJob(id, pullRequest, status, reviewMode, durationMs, tokenCount, errorMessage, createdAt, updatedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public PullRequest getPullRequest() { return pullRequest; }
    public void setPullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; }
    public ReviewJobStatus getStatus() { return status; }
    public void setStatus(ReviewJobStatus status) { this.status = status; }
    public ReviewMode getReviewMode() { return reviewMode; }
    public void setReviewMode(ReviewMode reviewMode) { this.reviewMode = reviewMode; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public Integer getTokenCount() { return tokenCount; }
    public void setTokenCount(Integer tokenCount) { this.tokenCount = tokenCount; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
