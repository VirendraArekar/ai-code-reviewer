package com.aicode.reviewer.dto.repo;

public class PrChatResponse {
    private String response;

    public PrChatResponse() {}
    public PrChatResponse(String response) { this.response = response; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String response;
        public Builder response(String response) { this.response = response; return this; }
        public PrChatResponse build() { return new PrChatResponse(response); }
    }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}
