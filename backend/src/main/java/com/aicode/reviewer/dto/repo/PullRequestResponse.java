package com.aicode.reviewer.dto.repo;

import java.time.LocalDateTime;

public class PullRequestResponse {
    private String id;
    private String repositoryId;
    private String repositoryName;
    private int githubPrNumber;
    private String title;
    private String description;
    private String authorUsername;
    private String authorAvatarUrl;
    private String state;
    private String sourceBranch;
    private String targetBranch;
    private String htmlUrl;
    private int additions;
    private int deletions;
    private int changedFilesCount;
    private String latestReviewId;
    private String latestReviewStatus;
    private Integer qualityScore;
    private Integer securityScore;
    private int criticalCount;
    private int highCount;
    private int mediumCount;
    private int lowCount;
    private int infoCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PullRequestResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String repositoryId;
        private String repositoryName;
        private int githubPrNumber;
        private String title;
        private String description;
        private String authorUsername;
        private String authorAvatarUrl;
        private String state;
        private String sourceBranch;
        private String targetBranch;
        private String htmlUrl;
        private int additions;
        private int deletions;
        private int changedFilesCount;
        private String latestReviewId;
        private String latestReviewStatus;
        private Integer qualityScore;
        private Integer securityScore;
        private int criticalCount;
        private int highCount;
        private int mediumCount;
        private int lowCount;
        private int infoCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder repositoryId(String repositoryId) { this.repositoryId = repositoryId; return this; }
        public Builder repositoryName(String repositoryName) { this.repositoryName = repositoryName; return this; }
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
        public Builder latestReviewId(String latestReviewId) { this.latestReviewId = latestReviewId; return this; }
        public Builder latestReviewStatus(String latestReviewStatus) { this.latestReviewStatus = latestReviewStatus; return this; }
        public Builder qualityScore(Integer qualityScore) { this.qualityScore = qualityScore; return this; }
        public Builder securityScore(Integer securityScore) { this.securityScore = securityScore; return this; }
        public Builder criticalCount(int criticalCount) { this.criticalCount = criticalCount; return this; }
        public Builder highCount(int highCount) { this.highCount = highCount; return this; }
        public Builder mediumCount(int mediumCount) { this.mediumCount = mediumCount; return this; }
        public Builder lowCount(int lowCount) { this.lowCount = lowCount; return this; }
        public Builder infoCount(int infoCount) { this.infoCount = infoCount; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public PullRequestResponse build() {
            PullRequestResponse r = new PullRequestResponse();
            r.id = id; r.repositoryId = repositoryId; r.repositoryName = repositoryName;
            r.githubPrNumber = githubPrNumber; r.title = title; r.description = description;
            r.authorUsername = authorUsername; r.authorAvatarUrl = authorAvatarUrl;
            r.state = state; r.sourceBranch = sourceBranch; r.targetBranch = targetBranch;
            r.htmlUrl = htmlUrl; r.additions = additions; r.deletions = deletions;
            r.changedFilesCount = changedFilesCount; r.latestReviewId = latestReviewId;
            r.latestReviewStatus = latestReviewStatus; r.qualityScore = qualityScore;
            r.securityScore = securityScore; r.criticalCount = criticalCount;
            r.highCount = highCount; r.mediumCount = mediumCount; r.lowCount = lowCount;
            r.infoCount = infoCount; r.createdAt = createdAt; r.updatedAt = updatedAt;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getRepositoryName() { return repositoryName; }
    public void setRepositoryName(String repositoryName) { this.repositoryName = repositoryName; }
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
    public String getLatestReviewId() { return latestReviewId; }
    public void setLatestReviewId(String latestReviewId) { this.latestReviewId = latestReviewId; }
    public String getLatestReviewStatus() { return latestReviewStatus; }
    public void setLatestReviewStatus(String latestReviewStatus) { this.latestReviewStatus = latestReviewStatus; }
    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }
    public Integer getSecurityScore() { return securityScore; }
    public void setSecurityScore(Integer securityScore) { this.securityScore = securityScore; }
    public int getCriticalCount() { return criticalCount; }
    public void setCriticalCount(int criticalCount) { this.criticalCount = criticalCount; }
    public int getHighCount() { return highCount; }
    public void setHighCount(int highCount) { this.highCount = highCount; }
    public int getMediumCount() { return mediumCount; }
    public void setMediumCount(int mediumCount) { this.mediumCount = mediumCount; }
    public int getLowCount() { return lowCount; }
    public void setLowCount(int lowCount) { this.lowCount = lowCount; }
    public int getInfoCount() { return infoCount; }
    public void setInfoCount(int infoCount) { this.infoCount = infoCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
