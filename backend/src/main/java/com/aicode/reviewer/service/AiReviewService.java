package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.dto.review.ReviewFindingResponse;
import com.aicode.reviewer.entity.*;
import com.aicode.reviewer.entity.enums.FindingStatus;
import com.aicode.reviewer.entity.enums.ReviewJobStatus;
import com.aicode.reviewer.entity.enums.ReviewMode;
import com.aicode.reviewer.repository.*;
import com.aicode.reviewer.service.ai.AIProviderFactory;
import com.aicode.reviewer.service.ai.AIProviderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiReviewService {

    public AiReviewService(PullRequestRepository pullRequestRepository, PullRequestFileRepository pullRequestFileRepository, ReviewJobRepository reviewJobRepository, AiReviewRepository aiReviewRepository, ReviewFindingRepository reviewFindingRepository, AIProviderConfigRepository aiProviderConfigRepository, AIProviderFactory aiProviderFactory, EncryptionService encryptionService, NotificationService notificationService) {
        this.pullRequestRepository = pullRequestRepository;
        this.pullRequestFileRepository = pullRequestFileRepository;
        this.reviewJobRepository = reviewJobRepository;
        this.aiReviewRepository = aiReviewRepository;
        this.reviewFindingRepository = reviewFindingRepository;
        this.aiProviderConfigRepository = aiProviderConfigRepository;
        this.aiProviderFactory = aiProviderFactory;
        this.encryptionService = encryptionService;
        this.notificationService = notificationService;
    }


    private final PullRequestRepository pullRequestRepository;
    private final PullRequestFileRepository pullRequestFileRepository;
    private final ReviewJobRepository reviewJobRepository;
    private final AiReviewRepository aiReviewRepository;
    private final ReviewFindingRepository reviewFindingRepository;
    private final AIProviderConfigRepository aiProviderConfigRepository;
    private final AIProviderFactory aiProviderFactory;
    private final EncryptionService encryptionService;
    private final NotificationService notificationService;

    @Transactional
    public AiReviewResponse executeReview(String pullRequestId, ReviewMode mode) {
        PullRequest pr = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Pull request not found: " + pullRequestId));

        List<PullRequestFile> files = pullRequestFileRepository.findByPullRequestId(pullRequestId);
        RepositoryEntity repo = pr.getRepository();

        ReviewMode effectiveMode = mode != null ? mode : repo.getReviewMode();

        // Create initial job
        ReviewJob job = ReviewJob.builder()
                .id(UUID.randomUUID().toString())
                .pullRequest(pr)
                .status(ReviewJobStatus.PROCESSING)
                .reviewMode(effectiveMode)
                .build();
        reviewJobRepository.save(job);

        // Fetch AI Provider config if present
        AIProviderConfig providerConfig = aiProviderConfigRepository
                .findByOrganizationIdAndProvider(repo.getOrganization().getId(), repo.getAiProvider())
                .orElse(null);

        String apiKey = providerConfig != null && providerConfig.getEncryptedApiKey() != null
                ? encryptionService.decrypt(providerConfig.getEncryptedApiKey())
                : null;

        String model = providerConfig != null && providerConfig.getModelName() != null
                ? providerConfig.getModelName()
                : repo.getAiModel();

        String baseUrl = providerConfig != null ? providerConfig.getBaseUrl() : null;

        AIProviderService aiService = aiProviderFactory.getService(repo.getAiProvider());
        AiReviewResponse reviewResponse = aiService.analyzeDiff(
                pr,
                files,
                repo.getCustomInstructions(),
                effectiveMode,
                apiKey,
                model,
                baseUrl
        );

        // Save AiReview to MySQL
        AiReview aiReview = AiReview.builder()
                .id(UUID.randomUUID().toString())
                .pullRequest(pr)
                .job(job)
                .summary(reviewResponse.getSummary())
                .qualityScore(reviewResponse.getQualityScore())
                .securityScore(reviewResponse.getSecurityScore())
                .performanceScore(reviewResponse.getPerformanceScore())
                .architectureScore(reviewResponse.getArchitectureScore())
                .testScore(reviewResponse.getTestScore())
                .aiProvider(repo.getAiProvider())
                .aiModel(model)
                .durationMs(reviewResponse.getDurationMs())
                .tokenCount(reviewResponse.getTokenCount())
                .build();
        aiReviewRepository.save(aiReview);

        // Save findings to MySQL
        List<ReviewFindingResponse> savedFindings = new ArrayList<>();
        for (ReviewFindingResponse fDto : reviewResponse.getFindings()) {
            ReviewFinding finding = ReviewFinding.builder()
                    .id(UUID.randomUUID().toString())
                    .aiReview(aiReview)
                    .pullRequest(pr)
                    .repository(repo)
                    .filePath(fDto.getFilePath())
                    .lineNumber(fDto.getLineNumber())
                    .title(fDto.getTitle())
                    .description(fDto.getDescription())
                    .severity(fDto.getSeverity())
                    .category(fDto.getCategory())
                    .technicalExplanation(fDto.getTechnicalExplanation())
                    .whyItMatters(fDto.getWhyItMatters())
                    .potentialImpact(fDto.getPotentialImpact())
                    .recommendedSolution(fDto.getRecommendedSolution())
                    .originalCodeSnippet(fDto.getOriginalCodeSnippet())
                    .correctedCodeSnippet(fDto.getCorrectedCodeSnippet())
                    .confidenceScore(fDto.getConfidenceScore())
                    .status(FindingStatus.OPEN)
                    .build();
            reviewFindingRepository.save(finding);

            fDto.setId(finding.getId());
            fDto.setReviewId(aiReview.getId());
            savedFindings.add(fDto);
        }

        // Update Pull Request Scores & Repo Last Reviewed
        pr.setQualityScore(reviewResponse.getQualityScore());
        pr.setSecurityScore(reviewResponse.getSecurityScore());
        pullRequestRepository.save(pr);

        repo.setLastReviewAt(LocalDateTime.now());

        // Complete Job
        job.setStatus(ReviewJobStatus.COMPLETED);
        job.setDurationMs(reviewResponse.getDurationMs());
        job.setTokenCount(reviewResponse.getTokenCount());
        reviewJobRepository.save(job);

        reviewResponse.setId(aiReview.getId());
        reviewResponse.setJobStatus(ReviewJobStatus.COMPLETED);
        reviewResponse.setFindings(savedFindings);

        return reviewResponse;
    }

    public String chatWithPr(String pullRequestId, String userMessage) {
        PullRequest pr = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Pull request not found: " + pullRequestId));

        List<PullRequestFile> files = pullRequestFileRepository.findByPullRequestId(pullRequestId);
        RepositoryEntity repo = pr.getRepository();

        AIProviderService aiService = aiProviderFactory.getService(repo.getAiProvider());
        return aiService.chat(pr, files, userMessage, null, repo.getAiModel(), null);
    }
}
