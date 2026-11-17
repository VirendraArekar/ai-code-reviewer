package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.repo.PullRequestFileResponse;
import com.aicode.reviewer.dto.repo.PullRequestResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.RepositoryEntity;
import com.aicode.reviewer.entity.ReviewFinding;
import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.FindingStatus;
import com.aicode.reviewer.repository.PullRequestFileRepository;
import com.aicode.reviewer.repository.PullRequestRepository;
import com.aicode.reviewer.repository.ReviewFindingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PullRequestServiceTest {

    @Mock
    private PullRequestRepository pullRequestRepository;

    @Mock
    private PullRequestFileRepository pullRequestFileRepository;

    @Mock
    private ReviewFindingRepository reviewFindingRepository;

    @InjectMocks
    private PullRequestService pullRequestService;

    private RepositoryEntity testRepo;
    private PullRequest testPr;

    @BeforeEach
    void setUp() {
        testRepo = RepositoryEntity.builder()
                .id("repo-100")
                .name("frontend-app")
                .fullName("acme/frontend-app")
                .build();

        testPr = PullRequest.builder()
                .id("pr-100")
                .repository(testRepo)
                .githubPrNumber(42)
                .title("Fix auth token expiration bug")
                .authorUsername("octocat")
                .state("open")
                .sourceBranch("fix-auth")
                .targetBranch("main")
                .additions(25)
                .deletions(10)
                .changedFilesCount(2)
                .qualityScore(85)
                .securityScore(90)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetPullRequestsByRepoAndState() {
        when(pullRequestRepository.findByRepositoryIdAndState("repo-100", "open"))
                .thenReturn(List.of(testPr));
        when(reviewFindingRepository.findByPullRequestId("pr-100"))
                .thenReturn(List.of(
                        ReviewFinding.builder()
                                .id("finding-1")
                                .severity(FindingSeverity.HIGH)
                                .category(FindingCategory.SECURITY)
                                .status(FindingStatus.OPEN)
                                .build()
                ));

        List<PullRequestResponse> result = pullRequestService.getPullRequests("repo-100", "open", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(42, result.get(0).getGithubPrNumber());
        assertEquals(1, result.get(0).getHighCount());
        assertEquals("acme/frontend-app", result.get(0).getRepositoryName());
    }

    @Test
    void testGetPullRequestById() {
        when(pullRequestRepository.findById("pr-100")).thenReturn(Optional.of(testPr));
        when(reviewFindingRepository.findByPullRequestId("pr-100")).thenReturn(List.of());

        PullRequestResponse res = pullRequestService.getPullRequestById("pr-100");

        assertNotNull(res);
        assertEquals("Fix auth token expiration bug", res.getTitle());
        assertEquals(85, res.getQualityScore());
    }

    @Test
    void testGetPullRequestFiles() {
        PullRequestFile file = PullRequestFile.builder()
                .id("file-1")
                .pullRequest(testPr)
                .filename("src/auth/token.ts")
                .status("modified")
                .additions(15)
                .deletions(5)
                .patch("@@ -1,5 +1,15 @@")
                .findingsCount(1)
                .build();

        when(pullRequestFileRepository.findByPullRequestId("pr-100")).thenReturn(List.of(file));

        List<PullRequestFileResponse> files = pullRequestService.getPullRequestFiles("pr-100");

        assertNotNull(files);
        assertEquals(1, files.size());
        assertEquals("src/auth/token.ts", files.get(0).getFilename());
        assertEquals(15, files.get(0).getAdditions());
    }
}
