package com.aicode.reviewer.dto.settings;

import jakarta.validation.constraints.NotBlank;

public class TeamRequest {
    @NotBlank(message = "Team name is required")
    private String name;
    private String description;

    public TeamRequest() {}
    public TeamRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String name;
        private String description;
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public TeamRequest build() { return new TeamRequest(name, description); }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
