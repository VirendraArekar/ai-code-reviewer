package com.aicode.reviewer.dto.settings;

import java.time.LocalDateTime;

public class AuditLogResponse {
    private String id;
    private String userId;
    private String userEmail;
    private String action;
    private String entityType;
    private String entityId;
    private String ipAddress;
    private String details;
    private LocalDateTime createdAt;

    public AuditLogResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String userId;
        private String userEmail;
        private String action;
        private String entityType;
        private String entityId;
        private String ipAddress;
        private String details;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder entityType(String entityType) { this.entityType = entityType; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder details(String details) { this.details = details; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AuditLogResponse build() {
            AuditLogResponse r = new AuditLogResponse();
            r.id = id; r.userId = userId; r.userEmail = userEmail;
            r.action = action; r.entityType = entityType; r.entityId = entityId;
            r.ipAddress = ipAddress; r.details = details; r.createdAt = createdAt;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
