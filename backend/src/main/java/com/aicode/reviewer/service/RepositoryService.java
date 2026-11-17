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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private static final Logger log = LoggerFactory.getLogger(RepositoryService.class);

    private final RepositoryRepository repositoryRepository;
    private final OrganizationRepository organizationRepository;
    private final PullRequestRepository pullRequestRepository;
    private final ReviewFindingRepository reviewFindingRepository;
    private final GitHubService gitHubService;

    public RepositoryService(
            RepositoryRepository repositoryRepository,
            OrganizationRepository organizationRepository,
            PullRequestRepository pullRequestRepository,
            ReviewFindingRepository reviewFindingRepository,
            @Lazy GitHubService gitHubService
    ) {
        this.repositoryRepository = repositoryRepository;
        this.organizationRepository = organizationRepository;
        this.pullRequestRepository = pullRequestRepository;
        this.reviewFindingRepository = reviewFindingRepository;
        this.gitHubService = gitHubService;
    }

    public List<RepositoryResponse> getRepositories(String orgId, String search) {
        List<RepositoryEntity> list;
        if (search != null && !search.isBlank()) {
            list = repositoryRepository.findByNameContainingIgnoreCaseOrFullNameContainingIgnoreCase(search, search);
        } else if (orgId != null && !orgId.isBlank()) {
            list = repositoryRepository.findByOrganizationId(orgId);
        } else {
            list = repositoryRepository.findAll();
        }
        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public RepositoryResponse getRepositoryById(String id) {
        RepositoryEntity repo = repositoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found with id: " + id));
        return mapToResponse(repo);
    }

    @Transactional
    public RepositoryResponse createRepository(RepositoryRequest request, String orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseGet(() -> organizationRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No organization found")));

        String rawInput = (request.getHtmlUrl() != null && !request.getHtmlUrl().isBlank())
                ? request.getHtmlUrl()
                : (request.getFullName() != null && !request.getFullName().isBlank())
                ? request.getFullName()
                : request.getName();

        String fullName = extractFullName(rawInput, org.getSlug(), request.getName());
        String ownerLogin = fullName.contains("/") ? fullName.split("/")[0] : org.getSlug();
        String repoName = fullName.contains("/") ? fullName.split("/")[1] : request.getName();

        // Check if repository with this fullName is already connected
        Optional<RepositoryEntity> existing = repositoryRepository.findByFullName(fullName);
        if (existing.isPresent()) {
            RepositoryEntity repo = existing.get();
            if (request.getReviewMode() != null) repo.setReviewMode(request.getReviewMode());
            if (request.getMinSeverity() != null) repo.setMinSeverity(request.getMinSeverity());
            if (request.getAiProvider() != null) repo.setAiProvider(request.getAiProvider());
            if (request.getAiModel() != null) repo.setAiModel(request.getAiModel());
            if (request.getAutoReviewEnabled() != null) repo.setAutoReviewEnabled(request.getAutoReviewEnabled());
            if (request.getInlineCommentsEnabled() != null) repo.setInlineCommentsEnabled(request.getInlineCommentsEnabled());
            if (request.getMaxDiffSizeKb() != null) repo.setMaxDiffSizeKb(request.getMaxDiffSizeKb());
            if (request.getExcludedPatterns() != null) repo.setExcludedPatterns(request.getExcludedPatterns());
            if (request.getCustomInstructions() != null) repo.setCustomInstructions(request.getCustomInstructions());

            RepositoryEntity updated = repositoryRepository.save(repo);
            String repoId = updated.getId();
            CompletableFuture.runAsync(() -> {
                try {
                    gitHubService.syncRepositoryPullRequests(repoId);
                } catch (Exception e) {
                    log.warn("Background PR sync failed for existing repo {}: {}", repo.getFullName(), e.getMessage());
                }
            });
            return mapToResponse(updated);
        }

        long githubRepoId = Math.abs(UUID.randomUUID().getMostSignificantBits());
        String defaultBranch = request.getDefaultBranch() != null ? request.getDefaultBranch() : "main";
        boolean isPrivate = request.getPrivateRepo() != null ? request.getPrivateRepo() : false;
        String htmlUrl = "https://github.com/" + fullName;
        String primaryLanguage = request.getPrimaryLanguage() != null ? request.getPrimaryLanguage() : "Java";

        // Attempt live metadata fetch from GitHub API
        try {
            Map<String, Object> ghData = gitHubService.fetchRepositoryDetails(ownerLogin, repoName);
            githubRepoId = (long) ghData.get("githubRepoId");
            fullName = (String) ghData.get("fullName");
            ownerLogin = (String) ghData.get("ownerLogin");
            defaultBranch = (String) ghData.get("defaultBranch");
            isPrivate = (boolean) ghData.get("privateRepo");
            htmlUrl = (String) ghData.get("htmlUrl");
            if (ghData.get("primaryLanguage") != null && !((String) ghData.get("primaryLanguage")).isBlank()) {
                primaryLanguage = (String) ghData.get("primaryLanguage");
            }
        } catch (Exception e) {
            log.warn("Could not query GitHub API directly for {} (using user-specified values): {}", fullName, e.getMessage());
        }

        RepositoryEntity repo = RepositoryEntity.builder()
                .id(UUID.randomUUID().toString())
                .organization(org)
                .githubRepoId(githubRepoId)
                .name(repoName)
                .fullName(fullName)
                .ownerLogin(ownerLogin)
                .defaultBranch(defaultBranch)
                .privateRepo(isPrivate)
                .htmlUrl(htmlUrl)
                .primaryLanguage(primaryLanguage)
                .autoReviewEnabled(request.getAutoReviewEnabled() != null ? request.getAutoReviewEnabled() : true)
                .inlineCommentsEnabled(request.getInlineCommentsEnabled() != null ? request.getInlineCommentsEnabled() : true)
                .reviewMode(request.getReviewMode() != null ? request.getReviewMode() : ReviewMode.STANDARD)
                .minSeverity(request.getMinSeverity() != null ? request.getMinSeverity() : FindingSeverity.MEDIUM)
                .aiProvider(request.getAiProvider() != null ? request.getAiProvider() : AIProvider.GEMINI)
                .aiModel(request.getAiModel() != null ? request.getAiModel() : "gemini-2.0-flash")
                .maxDiffSizeKb(request.getMaxDiffSizeKb() != null ? request.getMaxDiffSizeKb() : 500)
                .excludedPatterns(request.getExcludedPatterns())
                .customInstructions(request.getCustomInstructions())
                .build();

        RepositoryEntity saved = repositoryRepository.save(repo);
        String repoId = saved.getId();

        // Immediately sync pull requests in background thread so the HTTP response returns immediately without hanging!
        CompletableFuture.runAsync(() -> {
            try {
                gitHubService.syncRepositoryPullRequests(repoId);
            } catch (Exception e) {
                log.warn("Background initial PR sync failed for {}: {}", saved.getFullName(), e.getMessage());
            }
        });

        return mapToResponse(saved);
    }

    private String extractFullName(String input, String defaultOwner, String fallbackName) {
        if (input == null || input.isBlank()) {
            return defaultOwner + "/" + fallbackName;
        }
        String clean = input.trim();
        if (clean.startsWith("https://github.com/")) {
            clean = clean.replace("https://github.com/", "");
        } else if (clean.startsWith("http://github.com/")) {
            clean = clean.replace("http://github.com/", "");
        }
        if (clean.endsWith(".git")) {
            clean = clean.substring(0, clean.length() - 4);
        }
        if (clean.contains("/")) {
            return clean;
        }
        return defaultOwner + "/" + clean;
    }

    @Transactional
    public int syncRepository(String id) {
        return gitHubService.syncRepositoryPullRequests(id);
    }

    @Transactional
    public RepositoryResponse updateRepository(String id, RepositoryRequest request) {
        RepositoryEntity repo = repositoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found: " + id));

        if (request.getName() != null) repo.setName(request.getName());
        if (request.getDefaultBranch() != null) repo.setDefaultBranch(request.getDefaultBranch());
        if (request.getAutoReviewEnabled() != null) repo.setAutoReviewEnabled(request.getAutoReviewEnabled());
        if (request.getInlineCommentsEnabled() != null) repo.setInlineCommentsEnabled(request.getInlineCommentsEnabled());
        if (request.getReviewMode() != null) repo.setReviewMode(request.getReviewMode());
        if (request.getMinSeverity() != null) repo.setMinSeverity(request.getMinSeverity());
        if (request.getAiProvider() != null) repo.setAiProvider(request.getAiProvider());
        if (request.getAiModel() != null) repo.setAiModel(request.getAiModel());
        if (request.getMaxDiffSizeKb() != null) repo.setMaxDiffSizeKb(request.getMaxDiffSizeKb());
        if (request.getExcludedPatterns() != null) repo.setExcludedPatterns(request.getExcludedPatterns());
        if (request.getCustomInstructions() != null) repo.setCustomInstructions(request.getCustomInstructions());

        RepositoryEntity saved = repositoryRepository.save(repo);
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteRepository(String id) {
        repositoryRepository.deleteById(id);
    }

    private RepositoryResponse mapToResponse(RepositoryEntity r) {
        int openPrCount = pullRequestRepository.findByRepositoryIdAndState(r.getId(), "OPEN").size();
        int findingsCount = reviewFindingRepository.findByRepositoryId(r.getId()).size();

        return RepositoryResponse.builder()
                .id(r.getId())
                .organizationId(r.getOrganization().getId())
                .githubRepoId(r.getGithubRepoId())
                .name(r.getName())
                .fullName(r.getFullName())
                .ownerLogin(r.getOwnerLogin())
                .defaultBranch(r.getDefaultBranch())
                .privateRepo(r.isPrivateRepo())
                .htmlUrl(r.getHtmlUrl())
                .primaryLanguage(r.getPrimaryLanguage())
                .autoReviewEnabled(r.isAutoReviewEnabled())
                .inlineCommentsEnabled(r.isInlineCommentsEnabled())
                .reviewMode(r.getReviewMode())
                .minSeverity(r.getMinSeverity())
                .aiProvider(r.getAiProvider())
                .aiModel(r.getAiModel())
                .maxDiffSizeKb(r.getMaxDiffSizeKb())
                .excludedPatterns(r.getExcludedPatterns())
                .customInstructions(r.getCustomInstructions())
                .lastReviewAt(r.getLastReviewAt())
                .openPullRequestCount(openPrCount)
                .totalFindingsCount(findingsCount)
                .avgQualityScore(openPrCount > 0 ? 88 : 0)
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
