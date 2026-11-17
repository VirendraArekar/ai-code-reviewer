package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.dto.review.ReviewFindingResponse;
import com.aicode.reviewer.entity.AiReview;
import com.aicode.reviewer.entity.ReviewFinding;
import com.aicode.reviewer.repository.AiReviewRepository;
import com.aicode.reviewer.repository.ReviewFindingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
@Tag(name = "Review Results", description = "Endpoints for fetching completed AI code review reports and findings")
public class ReviewController {

    public ReviewController(AiReviewRepository aiReviewRepository, ReviewFindingRepository reviewFindingRepository) {
        this.aiReviewRepository = aiReviewRepository;
        this.reviewFindingRepository = reviewFindingRepository;
    }


    private final AiReviewRepository aiReviewRepository;
    private final ReviewFindingRepository reviewFindingRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Get detailed AI review report by ID")
    public ResponseEntity<AiReviewResponse> getReviewById(@PathVariable String id) {
        AiReview rev = aiReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + id));

        List<ReviewFindingResponse> findings = reviewFindingRepository.findByAiReviewId(id)
                .stream()
                .map(f -> ReviewFindingResponse.builder()
                        .id(f.getId())
                        .reviewId(f.getAiReview().getId())
                        .repositoryId(f.getRepository().getId())
                        .pullRequestId(f.getPullRequest().getId())
                        .filePath(f.getFilePath())
                        .lineNumber(f.getLineNumber())
                        .title(f.getTitle())
                        .description(f.getDescription())
                        .severity(f.getSeverity())
                        .category(f.getCategory())
                        .technicalExplanation(f.getTechnicalExplanation())
                        .whyItMatters(f.getWhyItMatters())
                        .potentialImpact(f.getPotentialImpact())
                        .recommendedSolution(f.getRecommendedSolution())
                        .originalCodeSnippet(f.getOriginalCodeSnippet())
                        .correctedCodeSnippet(f.getCorrectedCodeSnippet())
                        .confidenceScore(f.getConfidenceScore())
                        .status(f.getStatus())
                        .commentsCount(f.getCommentsCount())
                        .createdAt(f.getCreatedAt())
                        .updatedAt(f.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(AiReviewResponse.builder()
                .id(rev.getId())
                .pullRequestId(rev.getPullRequest().getId())
                .summary(rev.getSummary())
                .qualityScore(rev.getQualityScore())
                .securityScore(rev.getSecurityScore())
                .performanceScore(rev.getPerformanceScore())
                .architectureScore(rev.getArchitectureScore())
                .testScore(rev.getTestScore())
                .findings(findings)
                .durationMs(rev.getDurationMs())
                .tokenCount(rev.getTokenCount())
                .aiProvider(rev.getAiProvider())
                .aiModel(rev.getAiModel())
                .createdAt(rev.getCreatedAt())
                .build());
    }
}
