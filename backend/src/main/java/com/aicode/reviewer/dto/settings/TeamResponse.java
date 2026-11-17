package com.aicode.reviewer.dto.settings;

import java.time.LocalDateTime;

public class TeamResponse {
    private String id;
    private String organizationId;
    private String name;
    private String description;
    private int memberCount;
    private int repositoryCount;
    private LocalDateTime createdAt;

    public TeamResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String organizationId;
        private String name;
        private String description;
        private int memberCount;
        private int repositoryCount;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder organizationId(String organizationId) { this.organizationId = organizationId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder memberCount(int memberCount) { this.memberCount = memberCount; return this; }
        public Builder repositoryCount(int repositoryCount) { this.repositoryCount = repositoryCount; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public TeamResponse build() {
            TeamResponse r = new TeamResponse();
            r.id = id; r.organizationId = organizationId; r.name = name;
            r.description = description; r.memberCount = memberCount;
            r.repositoryCount = repositoryCount; r.createdAt = createdAt;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public int getRepositoryCount() { return repositoryCount; }
    public void setRepositoryCount(int repositoryCount) { this.repositoryCount = repositoryCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
