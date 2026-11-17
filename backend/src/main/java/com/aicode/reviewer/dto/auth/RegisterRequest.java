package com.aicode.reviewer.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String organizationName;

    public RegisterRequest() {}
    public RegisterRequest(String fullName, String email, String password, String organizationName) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.organizationName = organizationName;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String fullName;
        private String email;
        private String password;
        private String organizationName;
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder organizationName(String organizationName) { this.organizationName = organizationName; return this; }
        public RegisterRequest build() { return new RegisterRequest(fullName, email, password, organizationName); }
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
}
