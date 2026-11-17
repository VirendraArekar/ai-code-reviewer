package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.analytics.DashboardStatsResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.RepositoryEntity;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.repository.AiReviewRepository;
import com.aicode.reviewer.repository.PullRequestRepository;
import com.aicode.reviewer.repository.RepositoryRepository;
import com.aicode.reviewer.repository.ReviewFindingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private RepositoryRepository repositoryRepository;

    @Mock
    private PullRequestRepository pullRequestRepository;

    @Mock
    private AiReviewRepository aiReviewRepository;

    @Mock
    private ReviewFindingRepository reviewFindingRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void testGetDashboardStats() {
        when(repositoryRepository.count()).thenReturn(5L);
        when(pullRequestRepository.count()).thenReturn(24L);
        when(aiReviewRepository.count()).thenReturn(40L);
        when(reviewFindingRepository.count()).thenReturn(112L);
        when(reviewFindingRepository.countBySeverity(FindingSeverity.CRITICAL)).thenReturn(3L);

        PullRequest pr1 = PullRequest.builder().id("pr-1").qualityScore(90).build();
        PullRequest pr2 = PullRequest.builder().id("pr-2").qualityScore(80).build();
        when(pullRequestRepository.findAll()).thenReturn(List.of(pr1, pr2));

        when(reviewFindingRepository.countGroupBySeverity()).thenReturn(List.of(
                new Object[]{FindingSeverity.CRITICAL, 3L},
                new Object[]{FindingSeverity.HIGH, 15L}
        ));

        when(reviewFindingRepository.countGroupByCategory()).thenReturn(List.of(
                new Object[]{"SECURITY", 20L},
                new Object[]{"BUG", 35L}
        ));

        RepositoryEntity repo = RepositoryEntity.builder().id("repo-1").name("api-gateway").build();
        when(repositoryRepository.findAll()).thenReturn(List.of(repo));
        when(reviewFindingRepository.findByRepositoryId("repo-1")).thenReturn(List.of());

        DashboardStatsResponse stats = analyticsService.getDashboardStats();

        assertNotNull(stats);
        assertEquals(5L, stats.getTotalRepositories());
        assertEquals(24L, stats.getTotalPullRequests());
        assertEquals(40L, stats.getTotalReviews());
        assertEquals(112L, stats.getTotalIssuesDetected());
        assertEquals(3L, stats.getCriticalVulnerabilities());
        assertEquals(85, stats.getAvgQualityScore()); // (90 + 80) / 2
        assertEquals(2, stats.getIssuesBySeverity().size());
        assertEquals(2, stats.getIssuesByCategory().size());
        assertEquals(1, stats.getQualityByRepository().size());
    }
}
