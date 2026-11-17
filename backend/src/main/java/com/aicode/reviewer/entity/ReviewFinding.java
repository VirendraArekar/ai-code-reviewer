package com.aicode.reviewer.entity;

import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.FindingStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_findings")
public class ReviewFinding {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_review_id", nullable = false)
    private AiReview aiReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id", nullable = false)
    private PullRequest pullRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private RepositoryEntity repository;

    @Column(name = "file_path", nullable = false, length = 1024)
    private String filePath;

    @Column(name = "line_number", nullable = false)
    private int lineNumber;

    @Column(nullable = false, length = 512)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FindingSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FindingCategory category;

    @Column(name = "technical_explanation", columnDefinition = "TEXT", nullable = false)
    private String technicalExplanation;

    @Column(name = "why_it_matters", columnDefinition = "TEXT", nullable = false)
    private String whyItMatters;

    @Column(name = "potential_impact", columnDefinition = "TEXT", nullable = false)
    private String potentialImpact;

    @Column(name = "recommended_solution", columnDefinition = "TEXT", nullable = false)
    private String recommendedSolution;

    @Column(name = "original_code_snippet", columnDefinition = "TEXT")
    private String originalCodeSnippet;

    @Column(name = "corrected_code_snippet", columnDefinition = "TEXT")
    private String correctedCodeSnippet;

    @Column(name = "confidence_score", precision = 4, scale = 3, nullable = false)
    private BigDecimal confidenceScore = new BigDecimal("0.900");

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FindingStatus status = FindingStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedToUser;

    @Column(name = "comments_count", nullable = false)
    private int commentsCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ReviewFinding() {}

    public ReviewFinding(String id, AiReview aiReview, PullRequest pullRequest, RepositoryEntity repository, String filePath, int lineNumber, String title, String description, FindingSeverity severity, FindingCategory category, String technicalExplanation, String whyItMatters, String potentialImpact, String recommendedSolution, String originalCodeSnippet, String correctedCodeSnippet, BigDecimal confidenceScore, FindingStatus status, User assignedToUser, int commentsCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.aiReview = aiReview;
        this.pullRequest = pullRequest;
        this.repository = repository;
        this.filePath = filePath;
        this.lineNumber = lineNumber;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.category = category;
        this.technicalExplanation = technicalExplanation;
        this.whyItMatters = whyItMatters;
        this.potentialImpact = potentialImpact;
        this.recommendedSolution = recommendedSolution;
        this.originalCodeSnippet = originalCodeSnippet;
        this.correctedCodeSnippet = correctedCodeSnippet;
        this.confidenceScore = confidenceScore != null ? confidenceScore : new BigDecimal("0.900");
        this.status = status != null ? status : FindingStatus.OPEN;
        this.assignedToUser = assignedToUser;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private AiReview aiReview;
        private PullRequest pullRequest;
        private RepositoryEntity repository;
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
        private BigDecimal confidenceScore = new BigDecimal("0.900");
        private FindingStatus status = FindingStatus.OPEN;
        private User assignedToUser;
        private int commentsCount = 0;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder aiReview(AiReview aiReview) { this.aiReview = aiReview; return this; }
        public Builder pullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; return this; }
        public Builder repository(RepositoryEntity repository) { this.repository = repository; return this; }
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
        public Builder assignedToUser(User assignedToUser) { this.assignedToUser = assignedToUser; return this; }
        public Builder commentsCount(int commentsCount) { this.commentsCount = commentsCount; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public ReviewFinding build() { return new ReviewFinding(id, aiReview, pullRequest, repository, filePath, lineNumber, title, description, severity, category, technicalExplanation, whyItMatters, potentialImpact, recommendedSolution, originalCodeSnippet, correctedCodeSnippet, confidenceScore, status, assignedToUser, commentsCount, createdAt, updatedAt); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public AiReview getAiReview() { return aiReview; }
    public void setAiReview(AiReview aiReview) { this.aiReview = aiReview; }
    public PullRequest getPullRequest() { return pullRequest; }
    public void setPullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; }
    public RepositoryEntity getRepository() { return repository; }
    public void setRepository(RepositoryEntity repository) { this.repository = repository; }
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
    public User getAssignedToUser() { return assignedToUser; }
    public void setAssignedToUser(User assignedToUser) { this.assignedToUser = assignedToUser; }
    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
