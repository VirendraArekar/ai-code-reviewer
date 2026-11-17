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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    public RepositoryService(RepositoryRepository repositoryRepository, OrganizationRepository organizationRepository, PullRequestRepository pullRequestRepository, ReviewFindingRepository reviewFindingRepository) {
        this.repositoryRepository = repositoryRepository;
        this.organizationRepository = organizationRepository;
        this.pullRequestRepository = pullRequestRepository;
        this.reviewFindingRepository = reviewFindingRepository;
    }


    private final RepositoryRepository repositoryRepository;
    private final OrganizationRepository organizationRepository;
    private final PullRequestRepository pullRequestRepository;
    private final ReviewFindingRepository reviewFindingRepository;

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

        String fullName = request.getFullName() != null && !request.getFullName().isBlank()
                ? request.getFullName()
                : org.getSlug() + "/" + request.getName();

        RepositoryEntity repo = RepositoryEntity.builder()
                .id(UUID.randomUUID().toString())
                .organization(org)
                .githubRepoId(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                .name(request.getName())
                .fullName(fullName)
                .ownerLogin(fullName.split("/")[0])
                .defaultBranch(request.getDefaultBranch() != null ? request.getDefaultBranch() : "main")
                .privateRepo(request.getPrivateRepo() != null ? request.getPrivateRepo() : false)
                .htmlUrl(request.getHtmlUrl() != null ? request.getHtmlUrl() : "https://github.com/" + fullName)
                .primaryLanguage(request.getPrimaryLanguage() != null ? request.getPrimaryLanguage() : "Java")
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
        return mapToResponse(saved);
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
                .avgQualityScore(88)
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
