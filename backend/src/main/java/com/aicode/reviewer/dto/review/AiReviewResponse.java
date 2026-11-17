package com.aicode.reviewer.dto.review;

import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.ReviewJobStatus;
import java.time.LocalDateTime;
import java.util.List;

public class AiReviewResponse {
    private String id;
    private String pullRequestId;
    private ReviewJobStatus jobStatus;
    private String summary;
    private int qualityScore;
    private int securityScore;
    private int performanceScore;
    private int architectureScore;
    private int testScore;
    private List<ReviewFindingResponse> findings;
    private long durationMs;
    private int tokenCount;
    private AIProvider aiProvider;
    private String aiModel;
    private LocalDateTime createdAt;

    public AiReviewResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String pullRequestId;
        private ReviewJobStatus jobStatus;
        private String summary;
        private int qualityScore;
        private int securityScore;
        private int performanceScore;
        private int architectureScore;
        private int testScore;
        private List<ReviewFindingResponse> findings;
        private long durationMs;
        private int tokenCount;
        private AIProvider aiProvider;
        private String aiModel;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder pullRequestId(String pullRequestId) { this.pullRequestId = pullRequestId; return this; }
        public Builder jobStatus(ReviewJobStatus jobStatus) { this.jobStatus = jobStatus; return this; }
        public Builder summary(String summary) { this.summary = summary; return this; }
        public Builder qualityScore(int qualityScore) { this.qualityScore = qualityScore; return this; }
        public Builder securityScore(int securityScore) { this.securityScore = securityScore; return this; }
        public Builder performanceScore(int performanceScore) { this.performanceScore = performanceScore; return this; }
        public Builder architectureScore(int architectureScore) { this.architectureScore = architectureScore; return this; }
        public Builder testScore(int testScore) { this.testScore = testScore; return this; }
        public Builder findings(List<ReviewFindingResponse> findings) { this.findings = findings; return this; }
        public Builder durationMs(long durationMs) { this.durationMs = durationMs; return this; }
        public Builder tokenCount(int tokenCount) { this.tokenCount = tokenCount; return this; }
        public Builder aiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; return this; }
        public Builder aiModel(String aiModel) { this.aiModel = aiModel; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AiReviewResponse build() {
            AiReviewResponse r = new AiReviewResponse();
            r.id = id; r.pullRequestId = pullRequestId; r.jobStatus = jobStatus;
            r.summary = summary; r.qualityScore = qualityScore; r.securityScore = securityScore;
            r.performanceScore = performanceScore; r.architectureScore = architectureScore;
            r.testScore = testScore; r.findings = findings; r.durationMs = durationMs;
            r.tokenCount = tokenCount; r.aiProvider = aiProvider; r.aiModel = aiModel;
            r.createdAt = createdAt;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPullRequestId() { return pullRequestId; }
    public void setPullRequestId(String pullRequestId) { this.pullRequestId = pullRequestId; }
    public ReviewJobStatus getJobStatus() { return jobStatus; }
    public void setJobStatus(ReviewJobStatus jobStatus) { this.jobStatus = jobStatus; }
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
    public List<ReviewFindingResponse> getFindings() { return findings; }
    public void setFindings(List<ReviewFindingResponse> findings) { this.findings = findings; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
    public int getTokenCount() { return tokenCount; }
    public void setTokenCount(int tokenCount) { this.tokenCount = tokenCount; }
    public AIProvider getAiProvider() { return aiProvider; }
    public void setAiProvider(AIProvider aiProvider) { this.aiProvider = aiProvider; }
    public String getAiModel() { return aiModel; }
    public void setAiModel(String aiModel) { this.aiModel = aiModel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
