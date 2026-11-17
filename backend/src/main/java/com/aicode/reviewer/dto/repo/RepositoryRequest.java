package com.aicode.reviewer.dto.repo;

import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.ReviewMode;
import jakarta.validation.constraints.NotBlank;

public class RepositoryRequest {
    @NotBlank(message = "Repository name is required")
    private String name;
    private String fullName;
    private String htmlUrl;
    private String defaultBranch;
    private Boolean privateRepo;
    private String primaryLanguage;
    private Boolean autoReviewEnabled;
    private Boolean inlineCommentsEnabled;
    private ReviewMode reviewMode;
    private FindingSeverity minSeverity;
    private AIProvider aiProvider;
    private String aiModel;
    private Integer maxDiffSizeKb;
    private String excludedPatterns;
    private String customInstructions;

    public RepositoryRequest() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String name;
        private String fullName;
        private String htmlUrl;
        private String defaultBranch;
        private Boolean privateRepo;
        private String primaryLanguage;
        private Boolean autoReviewEnabled;
        private Boolean inlineCommentsEnabled;
        private ReviewMode reviewMode;
        private FindingSeverity minSeverity;
        private AIProvider aiProvider;
        private String aiModel;
        private Integer maxDiffSizeKb;
        private String excludedPatterns;
        private String customInstructions;

        public Builder name(String name) { this.name = name; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder htmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; return this; }
        public Builder defaultBranch(String defaultBranch) { this.defaultBranch = defaultBranch; return this; }
        public Builder privateRepo(Boolean privateRepo) { this.privateRepo = privateRepo; return this; }
        public Builder primaryLanguage(String primaryLanguage) { this.primaryLanguage = primaryLanguage; return this; }
        public Builder autoReviewEnabled(Boolean autoReviewEnabled) { this.autoReviewEnabled = autoReviewEnabled; return this; }
        public Builder inlineCommentsEnabled(Boolean inlineCommentsEnabled) { this.inlineCommentsEnabled = inlineCommentsEnabled; return this; }
        public Builder reviewMode(ReviewMode reviewMode) { this.reviewMode = reviewMode; return this; }
        public Builder minSeverity(FindingSeverity minSeverity) { this.minSeverity = minSeverity; return this; }
        public Builder aiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; return this; }
        public Builder aiModel(String aiModel) { this.aiModel = aiModel; return this; }
        public Builder maxDiffSizeKb(Integer maxDiffSizeKb) { this.maxDiffSizeKb = maxDiffSizeKb; return this; }
        public Builder excludedPatterns(String excludedPatterns) { this.excludedPatterns = excludedPatterns; return this; }
        public Builder customInstructions(String customInstructions) { this.customInstructions = customInstructions; return this; }
        public RepositoryRequest build() {
            RepositoryRequest req = new RepositoryRequest();
            req.setName(name);
            req.setFullName(fullName);
            req.setHtmlUrl(htmlUrl);
            req.setDefaultBranch(defaultBranch);
            req.setPrivateRepo(privateRepo);
            req.setPrimaryLanguage(primaryLanguage);
            req.setAutoReviewEnabled(autoReviewEnabled);
            req.setInlineCommentsEnabled(inlineCommentsEnabled);
            req.setReviewMode(reviewMode);
            req.setMinSeverity(minSeverity);
            req.setAiProvider(aiProvider);
            req.setAiModel(aiModel);
            req.setMaxDiffSizeKb(maxDiffSizeKb);
            req.setExcludedPatterns(excludedPatterns);
            req.setCustomInstructions(customInstructions);
            return req;
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }
    public String getDefaultBranch() { return defaultBranch; }
    public void setDefaultBranch(String defaultBranch) { this.defaultBranch = defaultBranch; }
    public Boolean getPrivateRepo() { return privateRepo; }
    public void setPrivateRepo(Boolean privateRepo) { this.privateRepo = privateRepo; }
    public String getPrimaryLanguage() { return primaryLanguage; }
    public void setPrimaryLanguage(String primaryLanguage) { this.primaryLanguage = primaryLanguage; }
    public Boolean getAutoReviewEnabled() { return autoReviewEnabled; }
    public void setAutoReviewEnabled(Boolean autoReviewEnabled) { this.autoReviewEnabled = autoReviewEnabled; }
    public Boolean getInlineCommentsEnabled() { return inlineCommentsEnabled; }
    public void setInlineCommentsEnabled(Boolean inlineCommentsEnabled) { this.inlineCommentsEnabled = inlineCommentsEnabled; }
    public ReviewMode getReviewMode() { return reviewMode; }
    public void setReviewMode(ReviewMode reviewMode) { this.reviewMode = reviewMode; }
    public FindingSeverity getMinSeverity() { return minSeverity; }
    public void setMinSeverity(FindingSeverity minSeverity) { this.minSeverity = minSeverity; }
    public AIProvider getAiProvider() { return aiProvider; }
    public void setAiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; }
    public String getAiModel() { return aiModel; }
    public void setAiModel(String aiModel) { this.aiModel = aiModel; }
    public Integer getMaxDiffSizeKb() { return maxDiffSizeKb; }
    public void setMaxDiffSizeKb(Integer maxDiffSizeKb) { this.maxDiffSizeKb = maxDiffSizeKb; }
    public String getExcludedPatterns() { return excludedPatterns; }
    public void setExcludedPatterns(String excludedPatterns) { this.excludedPatterns = excludedPatterns; }
    public String getCustomInstructions() { return customInstructions; }
    public void setCustomInstructions(String customInstructions) { this.customInstructions = customInstructions; }
}
