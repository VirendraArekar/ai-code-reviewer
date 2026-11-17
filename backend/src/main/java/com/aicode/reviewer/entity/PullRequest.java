package com.aicode.reviewer.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pull_requests")
public class PullRequest {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private RepositoryEntity repository;

    @Column(name = "github_pr_number", nullable = false)
    private int githubPrNumber;

    @Column(nullable = false, length = 512)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "author_username", nullable = false)
    private String authorUsername;

    @Column(name = "author_avatar_url", length = 1024)
    private String authorAvatarUrl;

    @Column(nullable = false, length = 50)
    private String state = "OPEN";

    @Column(name = "source_branch", nullable = false)
    private String sourceBranch;

    @Column(name = "target_branch", nullable = false)
    private String targetBranch;

    @Column(name = "html_url", nullable = false, length = 1024)
    private String htmlUrl;

    @Column(nullable = false)
    private int additions = 0;

    @Column(nullable = false)
    private int deletions = 0;

    @Column(name = "changed_files_count", nullable = false)
    private int changedFilesCount = 0;

    @Column(name = "quality_score")
    private Integer qualityScore = 0;

    @Column(name = "security_score")
    private Integer securityScore = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PullRequest() {}

    public PullRequest(String id, RepositoryEntity repository, int githubPrNumber, String title, String description, String authorUsername, String authorAvatarUrl, String state, String sourceBranch, String targetBranch, String htmlUrl, int additions, int deletions, int changedFilesCount, Integer qualityScore, Integer securityScore, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.repository = repository;
        this.githubPrNumber = githubPrNumber;
        this.title = title;
        this.description = description;
        this.authorUsername = authorUsername;
        this.authorAvatarUrl = authorAvatarUrl;
        this.state = state != null ? state : "OPEN";
        this.sourceBranch = sourceBranch;
        this.targetBranch = targetBranch;
        this.htmlUrl = htmlUrl;
        this.additions = additions;
        this.deletions = deletions;
        this.changedFilesCount = changedFilesCount;
        this.qualityScore = qualityScore != null ? qualityScore : 0;
        this.securityScore = securityScore != null ? securityScore : 0;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private RepositoryEntity repository;
        private int githubPrNumber;
        private String title;
        private String description;
        private String authorUsername;
        private String authorAvatarUrl;
        private String state = "OPEN";
        private String sourceBranch;
        private String targetBranch;
        private String htmlUrl;
        private int additions = 0;
        private int deletions = 0;
        private int changedFilesCount = 0;
        private Integer qualityScore = 0;
        private Integer securityScore = 0;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder repository(RepositoryEntity repository) { this.repository = repository; return this; }
        public Builder githubPrNumber(int githubPrNumber) { this.githubPrNumber = githubPrNumber; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder authorUsername(String authorUsername) { this.authorUsername = authorUsername; return this; }
        public Builder authorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder sourceBranch(String sourceBranch) { this.sourceBranch = sourceBranch; return this; }
        public Builder targetBranch(String targetBranch) { this.targetBranch = targetBranch; return this; }
        public Builder htmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; return this; }
        public Builder additions(int additions) { this.additions = additions; return this; }
        public Builder deletions(int deletions) { this.deletions = deletions; return this; }
        public Builder changedFilesCount(int changedFilesCount) { this.changedFilesCount = changedFilesCount; return this; }
        public Builder qualityScore(Integer qualityScore) { this.qualityScore = qualityScore; return this; }
        public Builder securityScore(Integer securityScore) { this.securityScore = securityScore; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public PullRequest build() { return new PullRequest(id, repository, githubPrNumber, title, description, authorUsername, authorAvatarUrl, state, sourceBranch, targetBranch, htmlUrl, additions, deletions, changedFilesCount, qualityScore, securityScore, createdAt, updatedAt); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public RepositoryEntity getRepository() { return repository; }
    public void setRepository(RepositoryEntity repository) { this.repository = repository; }
    public int getGithubPrNumber() { return githubPrNumber; }
    public void setGithubPrNumber(int githubPrNumber) { this.githubPrNumber = githubPrNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; }
    public void setAuthorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getSourceBranch() { return sourceBranch; }
    public void setSourceBranch(String sourceBranch) { this.sourceBranch = sourceBranch; }
    public String getTargetBranch() { return targetBranch; }
    public void setTargetBranch(String targetBranch) { this.targetBranch = targetBranch; }
    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }
    public int getAdditions() { return additions; }
    public void setAdditions(int additions) { this.additions = additions; }
    public int getDeletions() { return deletions; }
    public void setDeletions(int deletions) { this.deletions = deletions; }
    public int getChangedFilesCount() { return changedFilesCount; }
    public void setChangedFilesCount(int changedFilesCount) { this.changedFilesCount = changedFilesCount; }
    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }
    public Integer getSecurityScore() { return securityScore; }
    public void setSecurityScore(Integer securityScore) { this.securityScore = securityScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
