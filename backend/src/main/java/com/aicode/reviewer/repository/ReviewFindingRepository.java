package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.ReviewFinding;
import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.FindingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewFindingRepository extends JpaRepository<ReviewFinding, String> {
    List<ReviewFinding> findByAiReviewId(String aiReviewId);
    List<ReviewFinding> findByPullRequestId(String pullRequestId);
    List<ReviewFinding> findByRepositoryId(String repositoryId);
    List<ReviewFinding> findByStatus(FindingStatus status);

    long countBySeverity(FindingSeverity severity);
    long countByCategory(FindingCategory category);

    @Query("SELECT f.severity, COUNT(f) FROM ReviewFinding f GROUP BY f.severity")
    List<Object[]> countGroupBySeverity();

    @Query("SELECT f.category, COUNT(f) FROM ReviewFinding f GROUP BY f.category")
    List<Object[]> countGroupByCategory();

    @Query("SELECT f FROM ReviewFinding f WHERE " +
           "(:severity IS NULL OR f.severity = :severity) AND " +
           "(:category IS NULL OR f.category = :category) AND " +
           "(:status IS NULL OR f.status = :status)")
    List<ReviewFinding> findWithFilters(
            @Param("severity") FindingSeverity severity,
            @Param("category") FindingCategory category,
            @Param("status") FindingStatus status
    );
}
