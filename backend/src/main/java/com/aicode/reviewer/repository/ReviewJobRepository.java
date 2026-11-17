package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.ReviewJob;
import com.aicode.reviewer.entity.enums.ReviewJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJobRepository extends JpaRepository<ReviewJob, String> {
    List<ReviewJob> findByPullRequestIdOrderByCreatedAtDesc(String pullRequestId);
    List<ReviewJob> findByStatus(ReviewJobStatus status);
}
