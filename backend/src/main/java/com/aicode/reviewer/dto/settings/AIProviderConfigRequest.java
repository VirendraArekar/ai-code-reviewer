package com.aicode.reviewer.dto.settings;

import com.aicode.reviewer.entity.enums.AIProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AIProviderConfigRequest {
    @NotNull(message = "Provider is required")
    private AIProvider provider;
    @NotBlank(message = "Model name is required")
    private String modelName;
    private String apiKey;
    private String baseUrl;
    private Boolean isDefault;
    private Boolean enabled;

    public AIProviderConfigRequest() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private AIProvider provider;
        private String modelName;
        private String apiKey;
        private String baseUrl;
        private Boolean isDefault;
        private Boolean enabled;
        public Builder provider(AIProvider provider) { this.provider = provider; return this; }
        public Builder modelName(String modelName) { this.modelName = modelName; return this; }
        public Builder apiKey(String apiKey) { this.apiKey = apiKey; return this; }
        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public Builder isDefault(Boolean isDefault) { this.isDefault = isDefault; return this; }
        public Builder enabled(Boolean enabled) { this.enabled = enabled; return this; }
        public AIProviderConfigRequest build() {
            AIProviderConfigRequest r = new AIProviderConfigRequest();
            r.provider = provider; r.modelName = modelName; r.apiKey = apiKey;
            r.baseUrl = baseUrl; r.isDefault = isDefault; r.enabled = enabled;
            return r;
        }
    }

    public AIProvider getProvider() { return provider; }
    public void setProvider(AIProvider provider) { this.provider = provider; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean aDefault) { isDefault = aDefault; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
