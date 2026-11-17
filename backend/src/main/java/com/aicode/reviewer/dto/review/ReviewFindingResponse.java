package com.aicode.reviewer.dto.review;

import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.FindingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReviewFindingResponse {
    private String id;
    private String reviewId;
    private String repositoryId;
    private String pullRequestId;
    private String filePath;
    private int lineNumber;
    private String title;
    private String description;
    private FindingSeverity severity;
    private FindingCategory category;
    private String technicalExplanation;
    private String whyItMatters;
    private String potentialImpact;
    private String recommendedSolution;
    private String originalCodeSnippet;
    private String correctedCodeSnippet;
    private BigDecimal confidenceScore;
    private FindingStatus status;
    private String assignedToUserId;
    private String assignedToName;
    private int commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewFindingResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String reviewId;
        private String repositoryId;
        private String pullRequestId;
        private String filePath;
        private int lineNumber;
        private String title;
        private String description;
        private FindingSeverity severity;
        private FindingCategory category;
        private String technicalExplanation;
        private String whyItMatters;
        private String potentialImpact;
        private String recommendedSolution;
        private String originalCodeSnippet;
        private String correctedCodeSnippet;
        private BigDecimal confidenceScore;
        private FindingStatus status;
        private String assignedToUserId;
        private String assignedToName;
        private int commentsCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder reviewId(String reviewId) { this.reviewId = reviewId; return this; }
        public Builder repositoryId(String repositoryId) { this.repositoryId = repositoryId; return this; }
        public Builder pullRequestId(String pullRequestId) { this.pullRequestId = pullRequestId; return this; }
        public Builder filePath(String filePath) { this.filePath = filePath; return this; }
        public Builder lineNumber(int lineNumber) { this.lineNumber = lineNumber; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder severity(FindingSeverity severity) { this.severity = severity; return this; }
        public Builder category(FindingCategory category) { this.category = category; return this; }
        public Builder technicalExplanation(String technicalExplanation) { this.technicalExplanation = technicalExplanation; return this; }
        public Builder whyItMatters(String whyItMatters) { this.whyItMatters = whyItMatters; return this; }
        public Builder potentialImpact(String potentialImpact) { this.potentialImpact = potentialImpact; return this; }
        public Builder recommendedSolution(String recommendedSolution) { this.recommendedSolution = recommendedSolution; return this; }
        public Builder originalCodeSnippet(String originalCodeSnippet) { this.originalCodeSnippet = originalCodeSnippet; return this; }
        public Builder correctedCodeSnippet(String correctedCodeSnippet) { this.correctedCodeSnippet = correctedCodeSnippet; return this; }
        public Builder confidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public Builder status(FindingStatus status) { this.status = status; return this; }
        public Builder assignedToUserId(String assignedToUserId) { this.assignedToUserId = assignedToUserId; return this; }
        public Builder assignedToName(String assignedToName) { this.assignedToName = assignedToName; return this; }
        public Builder commentsCount(int commentsCount) { this.commentsCount = commentsCount; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ReviewFindingResponse build() {
            ReviewFindingResponse r = new ReviewFindingResponse();
            r.id = id; r.reviewId = reviewId; r.repositoryId = repositoryId;
            r.pullRequestId = pullRequestId; r.filePath = filePath; r.lineNumber = lineNumber;
            r.title = title; r.description = description; r.severity = severity;
            r.category = category; r.technicalExplanation = technicalExplanation;
            r.whyItMatters = whyItMatters; r.potentialImpact = potentialImpact;
            r.recommendedSolution = recommendedSolution; r.originalCodeSnippet = originalCodeSnippet;
            r.correctedCodeSnippet = correctedCodeSnippet; r.confidenceScore = confidenceScore;
            r.status = status; r.assignedToUserId = assignedToUserId;
            r.assignedToName = assignedToName; r.commentsCount = commentsCount;
            r.createdAt = createdAt; r.updatedAt = updatedAt;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getPullRequestId() { return pullRequestId; }
    public void setPullRequestId(String pullRequestId) { this.pullRequestId = pullRequestId; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public FindingSeverity getSeverity() { return severity; }
    public void setSeverity(FindingSeverity severity) { this.severity = severity; }
    public FindingCategory getCategory() { return category; }
    public void setCategory(FindingCategory category) { this.category = category; }
    public String getTechnicalExplanation() { return technicalExplanation; }
    public void setTechnicalExplanation(String technicalExplanation) { this.technicalExplanation = technicalExplanation; }
    public String getWhyItMatters() { return whyItMatters; }
    public void setWhyItMatters(String whyItMatters) { this.whyItMatters = whyItMatters; }
    public String getPotentialImpact() { return potentialImpact; }
    public void setPotentialImpact(String potentialImpact) { this.potentialImpact = potentialImpact; }
    public String getRecommendedSolution() { return recommendedSolution; }
    public void setRecommendedSolution(String recommendedSolution) { this.recommendedSolution = recommendedSolution; }
    public String getOriginalCodeSnippet() { return originalCodeSnippet; }
    public void setOriginalCodeSnippet(String originalCodeSnippet) { this.originalCodeSnippet = originalCodeSnippet; }
    public String getCorrectedCodeSnippet() { return correctedCodeSnippet; }
    public void setCorrectedCodeSnippet(String correctedCodeSnippet) { this.correctedCodeSnippet = correctedCodeSnippet; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public FindingStatus getStatus() { return status; }
    public void setStatus(FindingStatus status) { this.status = status; }
    public String getAssignedToUserId() { return assignedToUserId; }
    public void setAssignedToUserId(String assignedToUserId) { this.assignedToUserId = assignedToUserId; }
    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }
    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
