package com.aicode.reviewer.dto.analytics;

import java.util.List;
import java.util.Map;

public class DashboardStatsResponse {
    private long totalRepositories;
    private long totalPullRequests;
    private long totalReviews;
    private long totalIssuesDetected;
    private long criticalVulnerabilities;
    private int avgQualityScore;
    private int avgReviewDurationSeconds;
    private List<Map<String, Object>> issuesBySeverity;
    private List<Map<String, Object>> issuesByCategory;
    private List<Map<String, Object>> reviewsTrend;
    private List<Map<String, Object>> qualityByRepository;

    public DashboardStatsResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long totalRepositories;
        private long totalPullRequests;
        private long totalReviews;
        private long totalIssuesDetected;
        private long criticalVulnerabilities;
        private int avgQualityScore;
        private int avgReviewDurationSeconds;
        private List<Map<String, Object>> issuesBySeverity;
        private List<Map<String, Object>> issuesByCategory;
        private List<Map<String, Object>> reviewsTrend;
        private List<Map<String, Object>> qualityByRepository;

        public Builder totalRepositories(long totalRepositories) { this.totalRepositories = totalRepositories; return this; }
        public Builder totalPullRequests(long totalPullRequests) { this.totalPullRequests = totalPullRequests; return this; }
        public Builder totalReviews(long totalReviews) { this.totalReviews = totalReviews; return this; }
        public Builder totalIssuesDetected(long totalIssuesDetected) { this.totalIssuesDetected = totalIssuesDetected; return this; }
        public Builder criticalVulnerabilities(long criticalVulnerabilities) { this.criticalVulnerabilities = criticalVulnerabilities; return this; }
        public Builder avgQualityScore(int avgQualityScore) { this.avgQualityScore = avgQualityScore; return this; }
        public Builder avgReviewDurationSeconds(int avgReviewDurationSeconds) { this.avgReviewDurationSeconds = avgReviewDurationSeconds; return this; }
        public Builder issuesBySeverity(List<Map<String, Object>> issuesBySeverity) { this.issuesBySeverity = issuesBySeverity; return this; }
        public Builder issuesByCategory(List<Map<String, Object>> issuesByCategory) { this.issuesByCategory = issuesByCategory; return this; }
        public Builder reviewsTrend(List<Map<String, Object>> reviewsTrend) { this.reviewsTrend = reviewsTrend; return this; }
        public Builder qualityByRepository(List<Map<String, Object>> qualityByRepository) { this.qualityByRepository = qualityByRepository; return this; }

        public DashboardStatsResponse build() {
            DashboardStatsResponse r = new DashboardStatsResponse();
            r.totalRepositories = totalRepositories; r.totalPullRequests = totalPullRequests;
            r.totalReviews = totalReviews; r.totalIssuesDetected = totalIssuesDetected;
            r.criticalVulnerabilities = criticalVulnerabilities; r.avgQualityScore = avgQualityScore;
            r.avgReviewDurationSeconds = avgReviewDurationSeconds; r.issuesBySeverity = issuesBySeverity;
            r.issuesByCategory = issuesByCategory; r.reviewsTrend = reviewsTrend;
            r.qualityByRepository = qualityByRepository;
            return r;
        }
    }

    public long getTotalRepositories() { return totalRepositories; }
    public void setTotalRepositories(long totalRepositories) { this.totalRepositories = totalRepositories; }
    public long getTotalPullRequests() { return totalPullRequests; }
    public void setTotalPullRequests(long totalPullRequests) { this.totalPullRequests = totalPullRequests; }
    public long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(long totalReviews) { this.totalReviews = totalReviews; }
    public long getTotalIssuesDetected() { return totalIssuesDetected; }
    public void setTotalIssuesDetected(long totalIssuesDetected) { this.totalIssuesDetected = totalIssuesDetected; }
    public long getCriticalVulnerabilities() { return criticalVulnerabilities; }
    public void setCriticalVulnerabilities(long criticalVulnerabilities) { this.criticalVulnerabilities = criticalVulnerabilities; }
    public int getAvgQualityScore() { return avgQualityScore; }
    public void setAvgQualityScore(int avgQualityScore) { this.avgQualityScore = avgQualityScore; }
    public int getAvgReviewDurationSeconds() { return avgReviewDurationSeconds; }
    public void setAvgReviewDurationSeconds(int avgReviewDurationSeconds) { this.avgReviewDurationSeconds = avgReviewDurationSeconds; }
    public List<Map<String, Object>> getIssuesBySeverity() { return issuesBySeverity; }
    public void setIssuesBySeverity(List<Map<String, Object>> issuesBySeverity) { this.issuesBySeverity = issuesBySeverity; }
    public List<Map<String, Object>> getIssuesByCategory() { return issuesByCategory; }
    public void setIssuesByCategory(List<Map<String, Object>> issuesByCategory) { this.issuesByCategory = issuesByCategory; }
    public List<Map<String, Object>> getReviewsTrend() { return reviewsTrend; }
    public void setReviewsTrend(List<Map<String, Object>> reviewsTrend) { this.reviewsTrend = reviewsTrend; }
    public List<Map<String, Object>> getQualityByRepository() { return qualityByRepository; }
    public void setQualityByRepository(List<Map<String, Object>> qualityByRepository) { this.qualityByRepository = qualityByRepository; }
}
