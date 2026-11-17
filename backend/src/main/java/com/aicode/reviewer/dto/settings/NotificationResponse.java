package com.aicode.reviewer.dto.settings;

import java.time.LocalDateTime;

public class NotificationResponse {
    private String id;
    private String userId;
    private String title;
    private String message;
    private String type;
    private boolean read;
    private String actionUrl;
    private LocalDateTime createdAt;

    public NotificationResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String userId;
        private String title;
        private String message;
        private String type;
        private boolean read;
        private String actionUrl;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder read(boolean read) { this.read = read; return this; }
        public Builder actionUrl(String actionUrl) { this.actionUrl = actionUrl; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public NotificationResponse build() {
            NotificationResponse r = new NotificationResponse();
            r.id = id; r.userId = userId; r.title = title; r.message = message;
            r.type = type; r.read = read; r.actionUrl = actionUrl; r.createdAt = createdAt;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
