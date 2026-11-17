package com.aicode.reviewer.dto.repo;

import jakarta.validation.constraints.NotBlank;

public class PrChatRequest {
    @NotBlank(message = "Message cannot be empty")
    private String message;

    public PrChatRequest() {}
    public PrChatRequest(String message) { this.message = message; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String message;
        public Builder message(String message) { this.message = message; return this; }
        public PrChatRequest build() { return new PrChatRequest(message); }
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
