package com.aicode.reviewer.dto.repo;

import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.ReviewMode;
import java.time.LocalDateTime;

public class RepositoryResponse {
    private String id;
    private String organizationId;
    private Long githubRepoId;
    private String name;
    private String fullName;
    private String ownerLogin;
    private String defaultBranch;
    private boolean privateRepo;
    private String htmlUrl;
    private String primaryLanguage;
    private boolean autoReviewEnabled;
    private boolean inlineCommentsEnabled;
    private ReviewMode reviewMode;
    private FindingSeverity minSeverity;
    private AIProvider aiProvider;
    private String aiModel;
    private int maxDiffSizeKb;
    private String excludedPatterns;
    private String customInstructions;
    private LocalDateTime lastReviewAt;
    private int openPullRequestCount;
    private int totalFindingsCount;
    private Integer avgQualityScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RepositoryResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String organizationId;
        private Long githubRepoId;
        private String name;
        private String fullName;
        private String ownerLogin;
        private String defaultBranch;
        private boolean privateRepo;
        private String htmlUrl;
        private String primaryLanguage;
        private boolean autoReviewEnabled;
        private boolean inlineCommentsEnabled;
        private ReviewMode reviewMode;
        private FindingSeverity minSeverity;
        private AIProvider aiProvider;
        private String aiModel;
        private int maxDiffSizeKb;
        private String excludedPatterns;
        private String customInstructions;
        private LocalDateTime lastReviewAt;
        private int openPullRequestCount;
        private int totalFindingsCount;
        private Integer avgQualityScore;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder organizationId(String organizationId) { this.organizationId = organizationId; return this; }
        public Builder githubRepoId(Long githubRepoId) { this.githubRepoId = githubRepoId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder ownerLogin(String ownerLogin) { this.ownerLogin = ownerLogin; return this; }
        public Builder defaultBranch(String defaultBranch) { this.defaultBranch = defaultBranch; return this; }
        public Builder privateRepo(boolean privateRepo) { this.privateRepo = privateRepo; return this; }
        public Builder htmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; return this; }
        public Builder primaryLanguage(String primaryLanguage) { this.primaryLanguage = primaryLanguage; return this; }
        public Builder autoReviewEnabled(boolean autoReviewEnabled) { this.autoReviewEnabled = autoReviewEnabled; return this; }
        public Builder inlineCommentsEnabled(boolean inlineCommentsEnabled) { this.inlineCommentsEnabled = inlineCommentsEnabled; return this; }
        public Builder reviewMode(ReviewMode reviewMode) { this.reviewMode = reviewMode; return this; }
        public Builder minSeverity(FindingSeverity minSeverity) { this.minSeverity = minSeverity; return this; }
        public Builder aiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; return this; }
        public Builder aiModel(String aiModel) { this.aiModel = aiModel; return this; }
        public Builder maxDiffSizeKb(int maxDiffSizeKb) { this.maxDiffSizeKb = maxDiffSizeKb; return this; }
        public Builder excludedPatterns(String excludedPatterns) { this.excludedPatterns = excludedPatterns; return this; }
        public Builder customInstructions(String customInstructions) { this.customInstructions = customInstructions; return this; }
        public Builder lastReviewAt(LocalDateTime lastReviewAt) { this.lastReviewAt = lastReviewAt; return this; }
        public Builder openPullRequestCount(int openPullRequestCount) { this.openPullRequestCount = openPullRequestCount; return this; }
        public Builder totalFindingsCount(int totalFindingsCount) { this.totalFindingsCount = totalFindingsCount; return this; }
        public Builder avgQualityScore(Integer avgQualityScore) { this.avgQualityScore = avgQualityScore; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public RepositoryResponse build() {
            RepositoryResponse r = new RepositoryResponse();
            r.id = id; r.organizationId = organizationId; r.githubRepoId = githubRepoId;
            r.name = name; r.fullName = fullName; r.ownerLogin = ownerLogin;
            r.defaultBranch = defaultBranch; r.privateRepo = privateRepo; r.htmlUrl = htmlUrl;
            r.primaryLanguage = primaryLanguage; r.autoReviewEnabled = autoReviewEnabled;
            r.inlineCommentsEnabled = inlineCommentsEnabled; r.reviewMode = reviewMode;
            r.minSeverity = minSeverity; r.aiProvider = aiProvider; r.aiModel = aiModel;
            r.maxDiffSizeKb = maxDiffSizeKb; r.excludedPatterns = excludedPatterns;
            r.customInstructions = customInstructions; r.lastReviewAt = lastReviewAt;
            r.openPullRequestCount = openPullRequestCount; r.totalFindingsCount = totalFindingsCount;
            r.avgQualityScore = avgQualityScore; r.createdAt = createdAt; r.updatedAt = updatedAt;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
    public Long getGithubRepoId() { return githubRepoId; }
    public void setGithubRepoId(Long githubRepoId) { this.githubRepoId = githubRepoId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getOwnerLogin() { return ownerLogin; }
    public void setOwnerLogin(String ownerLogin) { this.ownerLogin = ownerLogin; }
    public String getDefaultBranch() { return defaultBranch; }
    public void setDefaultBranch(String defaultBranch) { this.defaultBranch = defaultBranch; }
    public boolean isPrivateRepo() { return privateRepo; }
    public void setPrivateRepo(boolean privateRepo) { this.privateRepo = privateRepo; }
    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }
    public String getPrimaryLanguage() { return primaryLanguage; }
    public void setPrimaryLanguage(String primaryLanguage) { this.primaryLanguage = primaryLanguage; }
    public boolean isAutoReviewEnabled() { return autoReviewEnabled; }
    public void setAutoReviewEnabled(boolean autoReviewEnabled) { this.autoReviewEnabled = autoReviewEnabled; }
    public boolean isInlineCommentsEnabled() { return inlineCommentsEnabled; }
    public void setInlineCommentsEnabled(boolean inlineCommentsEnabled) { this.inlineCommentsEnabled = inlineCommentsEnabled; }
    public ReviewMode getReviewMode() { return reviewMode; }
    public void setReviewMode(ReviewMode reviewMode) { this.reviewMode = reviewMode; }
    public FindingSeverity getMinSeverity() { return minSeverity; }
    public void setMinSeverity(FindingSeverity minSeverity) { this.minSeverity = minSeverity; }
    public AIProvider getAiProvider() { return aiProvider; }
    public void setAiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; }
    public String getAiModel() { return aiModel; }
    public void setAiModel(String aiModel) { this.aiModel = aiModel; }
    public int getMaxDiffSizeKb() { return maxDiffSizeKb; }
    public void setMaxDiffSizeKb(int maxDiffSizeKb) { this.maxDiffSizeKb = maxDiffSizeKb; }
    public String getExcludedPatterns() { return excludedPatterns; }
    public void setExcludedPatterns(String excludedPatterns) { this.excludedPatterns = excludedPatterns; }
    public String getCustomInstructions() { return customInstructions; }
    public void setCustomInstructions(String customInstructions) { this.customInstructions = customInstructions; }
    public LocalDateTime getLastReviewAt() { return lastReviewAt; }
    public void setLastReviewAt(LocalDateTime lastReviewAt) { this.lastReviewAt = lastReviewAt; }
    public int getOpenPullRequestCount() { return openPullRequestCount; }
    public void setOpenPullRequestCount(int openPullRequestCount) { this.openPullRequestCount = openPullRequestCount; }
    public int getTotalFindingsCount() { return totalFindingsCount; }
    public void setTotalFindingsCount(int totalFindingsCount) { this.totalFindingsCount = totalFindingsCount; }
    public Integer getAvgQualityScore() { return avgQualityScore; }
    public void setAvgQualityScore(Integer avgQualityScore) { this.avgQualityScore = avgQualityScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
