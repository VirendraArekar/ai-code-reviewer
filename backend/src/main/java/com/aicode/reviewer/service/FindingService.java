package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.review.ReviewFindingResponse;
import com.aicode.reviewer.dto.review.UpdateFindingStatusRequest;
import com.aicode.reviewer.entity.FindingComment;
import com.aicode.reviewer.entity.ReviewFinding;
import com.aicode.reviewer.entity.User;
import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.FindingStatus;
import com.aicode.reviewer.repository.FindingCommentRepository;
import com.aicode.reviewer.repository.ReviewFindingRepository;
import com.aicode.reviewer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FindingService {

    public FindingService(ReviewFindingRepository reviewFindingRepository, FindingCommentRepository findingCommentRepository, UserRepository userRepository) {
        this.reviewFindingRepository = reviewFindingRepository;
        this.findingCommentRepository = findingCommentRepository;
        this.userRepository = userRepository;
    }


    private final ReviewFindingRepository reviewFindingRepository;
    private final FindingCommentRepository findingCommentRepository;
    private final UserRepository userRepository;

    public List<ReviewFindingResponse> getFindings(
            FindingSeverity severity,
            FindingCategory category,
            FindingStatus status,
            String search
    ) {
        List<ReviewFinding> findings = reviewFindingRepository.findWithFilters(severity, category, status);

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            findings = findings.stream()
                    .filter(f -> f.getTitle().toLowerCase().contains(q) ||
                                 f.getFilePath().toLowerCase().contains(q) ||
                                 f.getDescription().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        return findings.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ReviewFindingResponse getFindingById(String id) {
        ReviewFinding finding = reviewFindingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Finding not found: " + id));
        return mapToResponse(finding);
    }

    @Transactional
    public ReviewFindingResponse updateStatus(String id, UpdateFindingStatusRequest request, String userId) {
        ReviewFinding finding = reviewFindingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Finding not found: " + id));

        finding.setStatus(request.getStatus());

        if (request.getComment() != null && !request.getComment().isBlank() && userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                FindingComment comment = FindingComment.builder()
                        .id(UUID.randomUUID().toString())
                        .finding(finding)
                        .user(user)
                        .comment(request.getComment())
                        .build();
                findingCommentRepository.save(comment);
                finding.setCommentsCount(finding.getCommentsCount() + 1);
            }
        }

        ReviewFinding saved = reviewFindingRepository.save(finding);
        return mapToResponse(saved);
    }

    @Transactional
    public ReviewFindingResponse assignFinding(String findingId, String userId) {
        ReviewFinding finding = reviewFindingRepository.findById(findingId)
                .orElseThrow(() -> new IllegalArgumentException("Finding not found: " + findingId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        finding.setAssignedToUser(user);
        ReviewFinding saved = reviewFindingRepository.save(finding);
        return mapToResponse(saved);
    }

    private ReviewFindingResponse mapToResponse(ReviewFinding f) {
        return ReviewFindingResponse.builder()
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
                .assignedToUserId(f.getAssignedToUser() != null ? f.getAssignedToUser().getId() : null)
                .assignedToName(f.getAssignedToUser() != null ? f.getAssignedToUser().getFullName() : null)
                .commentsCount(f.getCommentsCount())
                .createdAt(f.getCreatedAt())
                .updatedAt(f.getUpdatedAt())
                .build();
    }
}
