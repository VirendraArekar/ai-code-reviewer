package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.AiReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiReviewRepository extends JpaRepository<AiReview, String> {
    List<AiReview> findByPullRequestIdOrderByCreatedAtDesc(String pullRequestId);
    Optional<AiReview> findTopByPullRequestIdOrderByCreatedAtDesc(String pullRequestId);
}
