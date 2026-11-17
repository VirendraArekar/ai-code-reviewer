package com.aicode.reviewer.entity;

import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.ReviewMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "repositories")
public class RepositoryEntity {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "github_repo_id", nullable = false)
    private Long githubRepoId;

    @Column(nullable = false)
    private String name;

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "owner_login", nullable = false)
    private String ownerLogin;

    @Column(name = "default_branch", nullable = false, length = 100)
    private String defaultBranch = "main";

    @Column(name = "private_repo", nullable = false)
    private boolean privateRepo = false;

    @Column(name = "html_url", nullable = false, length = 1024)
    private String htmlUrl;

    @Column(name = "primary_language", length = 100)
    private String primaryLanguage;

    @Column(name = "auto_review_enabled", nullable = false)
    private boolean autoReviewEnabled = true;

    @Column(name = "inline_comments_enabled", nullable = false)
    private boolean inlineCommentsEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_mode", nullable = false, length = 50)
    private ReviewMode reviewMode = ReviewMode.STANDARD;

    @Enumerated(EnumType.STRING)
    @Column(name = "min_severity", nullable = false, length = 50)
    private FindingSeverity minSeverity = FindingSeverity.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_provider", nullable = false, length = 50)
    private AIProvider aiProvider = AIProvider.GEMINI;

    @Column(name = "ai_model", nullable = false, length = 100)
    private String aiModel = "gemini-2.0-flash";

    @Column(name = "max_diff_size_kb", nullable = false)
    private int maxDiffSizeKb = 500;

    @Column(name = "excluded_patterns", columnDefinition = "TEXT")
    private String excludedPatterns;

    @Column(name = "custom_instructions", columnDefinition = "TEXT")
    private String customInstructions;

    @Column(name = "last_review_at")
    private LocalDateTime lastReviewAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public RepositoryEntity() {}

    public RepositoryEntity(String id, Organization organization, Long githubRepoId, String name, String fullName, String ownerLogin, String defaultBranch, boolean privateRepo, String htmlUrl, String primaryLanguage, boolean autoReviewEnabled, boolean inlineCommentsEnabled, ReviewMode reviewMode, FindingSeverity minSeverity, AIProvider aiProvider, String aiModel, int maxDiffSizeKb, String excludedPatterns, String customInstructions, LocalDateTime lastReviewAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organization = organization;
        this.githubRepoId = githubRepoId;
        this.name = name;
        this.fullName = fullName;
        this.ownerLogin = ownerLogin;
        this.defaultBranch = defaultBranch != null ? defaultBranch : "main";
        this.privateRepo = privateRepo;
        this.htmlUrl = htmlUrl;
        this.primaryLanguage = primaryLanguage;
        this.autoReviewEnabled = autoReviewEnabled;
        this.inlineCommentsEnabled = inlineCommentsEnabled;
        this.reviewMode = reviewMode != null ? reviewMode : ReviewMode.STANDARD;
        this.minSeverity = minSeverity != null ? minSeverity : FindingSeverity.MEDIUM;
        this.aiProvider = aiProvider != null ? aiProvider : AIProvider.GEMINI;
        this.aiModel = aiModel != null ? aiModel : "gemini-2.0-flash";
        this.maxDiffSizeKb = maxDiffSizeKb > 0 ? maxDiffSizeKb : 500;
        this.excludedPatterns = excludedPatterns;
        this.customInstructions = customInstructions;
        this.lastReviewAt = lastReviewAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private Organization organization;
        private Long githubRepoId;
        private String name;
        private String fullName;
        private String ownerLogin;
        private String defaultBranch = "main";
        private boolean privateRepo = false;
        private String htmlUrl;
        private String primaryLanguage;
        private boolean autoReviewEnabled = true;
        private boolean inlineCommentsEnabled = true;
        private ReviewMode reviewMode = ReviewMode.STANDARD;
        private FindingSeverity minSeverity = FindingSeverity.MEDIUM;
        private AIProvider aiProvider = AIProvider.GEMINI;
        private String aiModel = "gemini-2.0-flash";
        private int maxDiffSizeKb = 500;
        private String excludedPatterns;
        private String customInstructions;
        private LocalDateTime lastReviewAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder organization(Organization organization) { this.organization = organization; return this; }
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
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public RepositoryEntity build() { return new RepositoryEntity(id, organization, githubRepoId, name, fullName, ownerLogin, defaultBranch, privateRepo, htmlUrl, primaryLanguage, autoReviewEnabled, inlineCommentsEnabled, reviewMode, minSeverity, aiProvider, aiModel, maxDiffSizeKb, excludedPatterns, customInstructions, lastReviewAt, createdAt, updatedAt); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
