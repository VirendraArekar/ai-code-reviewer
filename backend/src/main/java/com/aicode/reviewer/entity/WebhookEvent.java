package com.aicode.reviewer.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_events")
public class WebhookEvent {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "github_event", nullable = false, length = 100)
    private String githubEvent;

    @Column(name = "delivery_id", nullable = false, unique = true)
    private String deliveryId;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public WebhookEvent() {}

    public WebhookEvent(String id, String githubEvent, String deliveryId, String payload, boolean processed, String errorMessage, LocalDateTime createdAt) {
        this.id = id;
        this.githubEvent = githubEvent;
        this.deliveryId = deliveryId;
        this.payload = payload;
        this.processed = processed;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String githubEvent;
        private String deliveryId;
        private String payload;
        private boolean processed = false;
        private String errorMessage;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder githubEvent(String githubEvent) { this.githubEvent = githubEvent; return this; }
        public Builder deliveryId(String deliveryId) { this.deliveryId = deliveryId; return this; }
        public Builder payload(String payload) { this.payload = payload; return this; }
        public Builder processed(boolean processed) { this.processed = processed; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public WebhookEvent build() { return new WebhookEvent(id, githubEvent, deliveryId, payload, processed, errorMessage, createdAt); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getGithubEvent() { return githubEvent; }
    public void setGithubEvent(String githubEvent) { this.githubEvent = githubEvent; }
    public String getDeliveryId() { return deliveryId; }
    public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public boolean isProcessed() { return processed; }
    public void setProcessed(boolean processed) { this.processed = processed; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
