package com.aicode.reviewer.entity;

import com.aicode.reviewer.entity.enums.AIProvider;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_reviews")
public class AiReview {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id", nullable = false)
    private PullRequest pullRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private ReviewJob job;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String summary;

    @Column(name = "quality_score", nullable = false)
    private int qualityScore = 0;

    @Column(name = "security_score", nullable = false)
    private int securityScore = 0;

    @Column(name = "performance_score", nullable = false)
    private int performanceScore = 0;

    @Column(name = "architecture_score", nullable = false)
    private int architectureScore = 0;

    @Column(name = "test_score", nullable = false)
    private int testScore = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_provider", nullable = false, length = 50)
    private AIProvider aiProvider;

    @Column(name = "ai_model", nullable = false, length = 100)
    private String aiModel;

    @Column(name = "duration_ms")
    private Long durationMs = 0L;

    @Column(name = "token_count")
    private Integer tokenCount = 0;

    @Column(name = "raw_ai_response", columnDefinition = "LONGTEXT")
    private String rawAiResponse;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public AiReview() {}

    public AiReview(String id, PullRequest pullRequest, ReviewJob job, String summary,
                    int qualityScore, int securityScore, int performanceScore, int architectureScore, int testScore,
                    AIProvider aiProvider, String aiModel, Long durationMs, Integer tokenCount,
                    String rawAiResponse, LocalDateTime createdAt) {
        this.id = id;
        this.pullRequest = pullRequest;
        this.job = job;
        this.summary = summary;
        this.qualityScore = qualityScore;
        this.securityScore = securityScore;
        this.performanceScore = performanceScore;
        this.architectureScore = architectureScore;
        this.testScore = testScore;
        this.aiProvider = aiProvider;
        this.aiModel = aiModel;
        this.durationMs = durationMs != null ? durationMs : 0L;
        this.tokenCount = tokenCount != null ? tokenCount : 0;
        this.rawAiResponse = rawAiResponse;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private PullRequest pullRequest;
        private ReviewJob job;
        private String summary;
        private int qualityScore = 0;
        private int securityScore = 0;
        private int performanceScore = 0;
        private int architectureScore = 0;
        private int testScore = 0;
        private AIProvider aiProvider;
        private String aiModel;
        private Long durationMs = 0L;
        private Integer tokenCount = 0;
        private String rawAiResponse;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder pullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; return this; }
        public Builder job(ReviewJob job) { this.job = job; return this; }
        public Builder summary(String summary) { this.summary = summary; return this; }
        public Builder qualityScore(int qualityScore) { this.qualityScore = qualityScore; return this; }
        public Builder securityScore(int securityScore) { this.securityScore = securityScore; return this; }
        public Builder performanceScore(int performanceScore) { this.performanceScore = performanceScore; return this; }
        public Builder architectureScore(int architectureScore) { this.architectureScore = architectureScore; return this; }
        public Builder testScore(int testScore) { this.testScore = testScore; return this; }
        public Builder aiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; return this; }
        public Builder aiModel(String aiModel) { this.aiModel = aiModel; return this; }
        public Builder durationMs(Long durationMs) { this.durationMs = durationMs; return this; }
        public Builder tokenCount(Integer tokenCount) { this.tokenCount = tokenCount; return this; }
        public Builder rawAiResponse(String rawAiResponse) { this.rawAiResponse = rawAiResponse; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AiReview build() {
            return new AiReview(id, pullRequest, job, summary, qualityScore, securityScore,
                    performanceScore, architectureScore, testScore, aiProvider, aiModel, durationMs, tokenCount,
                    rawAiResponse, createdAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public PullRequest getPullRequest() { return pullRequest; }
    public void setPullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; }
    public ReviewJob getJob() { return job; }
    public void setJob(ReviewJob job) { this.job = job; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public int getQualityScore() { return qualityScore; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }
    public int getSecurityScore() { return securityScore; }
    public void setSecurityScore(int securityScore) { this.securityScore = securityScore; }
    public int getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(int performanceScore) { this.performanceScore = performanceScore; }
    public int getArchitectureScore() { return architectureScore; }
    public void setArchitectureScore(int architectureScore) { this.architectureScore = architectureScore; }
    public int getTestScore() { return testScore; }
    public void setTestScore(int testScore) { this.testScore = testScore; }
    public AIProvider getAiProvider() { return aiProvider; }
    public void setAiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; }
    public String getAiModel() { return aiModel; }
    public void setAiModel(String aiModel) { this.aiModel = aiModel; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public Integer getTokenCount() { return tokenCount; }
    public void setTokenCount(Integer tokenCount) { this.tokenCount = tokenCount; }
    public String getRawAiResponse() { return rawAiResponse; }
    public void setRawAiResponse(String rawAiResponse) { this.rawAiResponse = rawAiResponse; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
