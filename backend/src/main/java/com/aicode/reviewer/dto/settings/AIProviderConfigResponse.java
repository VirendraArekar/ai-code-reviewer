package com.aicode.reviewer.dto.settings;

import com.aicode.reviewer.entity.enums.AIProvider;

public class AIProviderConfigResponse {
    private String id;
    private AIProvider provider;
    private String modelName;
    private boolean hasApiKey;
    private String baseUrl;
    private boolean isDefault;
    private boolean enabled;

    public AIProviderConfigResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private AIProvider provider;
        private String modelName;
        private boolean hasApiKey;
        private String baseUrl;
        private boolean isDefault;
        private boolean enabled;
        public Builder id(String id) { this.id = id; return this; }
        public Builder provider(AIProvider provider) { this.provider = provider; return this; }
        public Builder modelName(String modelName) { this.modelName = modelName; return this; }
        public Builder hasApiKey(boolean hasApiKey) { this.hasApiKey = hasApiKey; return this; }
        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public Builder isDefault(boolean isDefault) { this.isDefault = isDefault; return this; }
        public Builder enabled(boolean enabled) { this.enabled = enabled; return this; }
        public AIProviderConfigResponse build() {
            AIProviderConfigResponse r = new AIProviderConfigResponse();
            r.id = id; r.provider = provider; r.modelName = modelName;
            r.hasApiKey = hasApiKey; r.baseUrl = baseUrl; r.isDefault = isDefault; r.enabled = enabled;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public AIProvider getProvider() { return provider; }
    public void setProvider(AIProvider provider) { this.provider = provider; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public boolean isHasApiKey() { return hasApiKey; }
    public void setHasApiKey(boolean hasApiKey) { this.hasApiKey = hasApiKey; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
