package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.review.ReviewFindingResponse;
import com.aicode.reviewer.dto.review.UpdateFindingStatusRequest;
import com.aicode.reviewer.entity.*;
import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.FindingStatus;
import com.aicode.reviewer.repository.FindingCommentRepository;
import com.aicode.reviewer.repository.ReviewFindingRepository;
import com.aicode.reviewer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindingServiceTest {

    @Mock
    private ReviewFindingRepository reviewFindingRepository;

    @Mock
    private FindingCommentRepository findingCommentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindingService findingService;

    private ReviewFinding testFinding;
    private User testUser;

    @BeforeEach
    void setUp() {
        RepositoryEntity repo = RepositoryEntity.builder().id("repo-1").build();
        PullRequest pr = PullRequest.builder().id("pr-1").build();
        AiReview review = AiReview.builder().id("review-1").build();

        testUser = User.builder().id("user-1").fullName("Lead Engineer").build();

        testFinding = ReviewFinding.builder()
                .id("finding-1")
                .repository(repo)
                .pullRequest(pr)
                .aiReview(review)
                .filePath("src/main/Auth.java")
                .lineNumber(45)
                .title("SQL Injection vulnerability")
                .description("Unescaped SQL query string concatenation")
                .severity(FindingSeverity.CRITICAL)
                .category(FindingCategory.SECURITY)
                .status(FindingStatus.OPEN)
                .confidenceScore(new BigDecimal("0.98"))
                .commentsCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetFindingsWithFilter() {
        when(reviewFindingRepository.findWithFilters(FindingSeverity.CRITICAL, FindingCategory.SECURITY, FindingStatus.OPEN))
                .thenReturn(List.of(testFinding));

        List<ReviewFindingResponse> findings = findingService.getFindings(FindingSeverity.CRITICAL, FindingCategory.SECURITY, FindingStatus.OPEN, null);

        assertNotNull(findings);
        assertEquals(1, findings.size());
        assertEquals("SQL Injection vulnerability", findings.get(0).getTitle());
        assertEquals(FindingSeverity.CRITICAL, findings.get(0).getSeverity());
    }

    @Test
    void testUpdateStatusWithComment() {
        UpdateFindingStatusRequest request = new UpdateFindingStatusRequest(FindingStatus.RESOLVED, "Fixed by parameterizing query");

        when(reviewFindingRepository.findById("finding-1")).thenReturn(Optional.of(testFinding));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));
        when(findingCommentRepository.save(any(FindingComment.class))).thenAnswer(i -> i.getArgument(0));
        when(reviewFindingRepository.save(any(ReviewFinding.class))).thenAnswer(i -> i.getArgument(0));

        ReviewFindingResponse response = findingService.updateStatus("finding-1", request, "user-1");

        assertNotNull(response);
        assertEquals(FindingStatus.RESOLVED, response.getStatus());
        assertEquals(1, response.getCommentsCount());
        verify(findingCommentRepository).save(any(FindingComment.class));
    }
}
