package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.repo.RepositoryRequest;
import com.aicode.reviewer.dto.repo.RepositoryResponse;
import com.aicode.reviewer.entity.Organization;
import com.aicode.reviewer.entity.RepositoryEntity;
import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.ReviewMode;
import com.aicode.reviewer.repository.OrganizationRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {

    @Mock
    private RepositoryRepository repositoryRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private PullRequestRepository pullRequestRepository;

    @Mock
    private ReviewFindingRepository reviewFindingRepository;

    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private RepositoryService repositoryService;

    private Organization testOrg;
    private RepositoryEntity testRepo;

    @BeforeEach
    void setUp() {
        testOrg = Organization.builder()
                .id("org-1")
                .name("Acme Engineering")
                .slug("acme-eng")
                .build();

        testRepo = RepositoryEntity.builder()
                .id("repo-1")
                .name("core-service")
                .fullName("acme/core-service")
                .githubRepoId(123456L)
                .defaultBranch("main")
                .primaryLanguage("Java")
                .autoReviewEnabled(true)
                .inlineCommentsEnabled(true)
                .reviewMode(ReviewMode.STANDARD)
                .minSeverity(FindingSeverity.MEDIUM)
                .aiProvider(AIProvider.GEMINI)
                .aiModel("gemini-1.5-pro")
                .organization(testOrg)
                .build();
    }

    @Test
    void testGetRepositoriesByOrg() {
        when(repositoryRepository.findByOrganizationId("org-1")).thenReturn(List.of(testRepo));
        when(pullRequestRepository.findByRepositoryIdAndState("repo-1", "OPEN")).thenReturn(List.of());
        when(reviewFindingRepository.findByRepositoryId("repo-1")).thenReturn(List.of());

        List<RepositoryResponse> repos = repositoryService.getRepositories("org-1", null);

        assertNotNull(repos);
        assertEquals(1, repos.size());
        assertEquals("acme/core-service", repos.get(0).getFullName());
        assertTrue(repos.get(0).isAutoReviewEnabled());
    }

    @Test
    void testCreateRepository() {
        RepositoryRequest request = RepositoryRequest.builder()
                .name("auth-service")
                .fullName("acme/auth-service")
                .defaultBranch("main")
                .primaryLanguage("TypeScript")
                .autoReviewEnabled(true)
                .inlineCommentsEnabled(false)
                .reviewMode(ReviewMode.DEEP)
                .minSeverity(FindingSeverity.HIGH)
                .aiProvider(AIProvider.OPENAI)
                .aiModel("gpt-4o")
                .build();

        when(organizationRepository.findById("org-1")).thenReturn(Optional.of(testOrg));
        when(repositoryRepository.save(any(RepositoryEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(pullRequestRepository.findByRepositoryIdAndState(any(), any())).thenReturn(List.of());
        when(reviewFindingRepository.findByRepositoryId(any())).thenReturn(List.of());

        RepositoryResponse response = repositoryService.createRepository(request, "org-1");

        assertNotNull(response);
        assertEquals("acme/auth-service", response.getFullName());
        assertEquals(ReviewMode.DEEP, response.getReviewMode());
        assertEquals("gpt-4o", response.getAiModel());
        verify(repositoryRepository).save(any(RepositoryEntity.class));
    }

    @Test
    void testUpdateRepository() {
        RepositoryRequest request = RepositoryRequest.builder()
                .autoReviewEnabled(false)
                .minSeverity(FindingSeverity.CRITICAL)
                .build();

        when(repositoryRepository.findById("repo-1")).thenReturn(Optional.of(testRepo));
        when(repositoryRepository.save(any(RepositoryEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(pullRequestRepository.findByRepositoryIdAndState(any(), any())).thenReturn(List.of());
        when(reviewFindingRepository.findByRepositoryId(any())).thenReturn(List.of());

        RepositoryResponse response = repositoryService.updateRepository("repo-1", request);

        assertNotNull(response);
        assertFalse(response.isAutoReviewEnabled());
        assertEquals(FindingSeverity.CRITICAL, response.getMinSeverity());
    }
}
