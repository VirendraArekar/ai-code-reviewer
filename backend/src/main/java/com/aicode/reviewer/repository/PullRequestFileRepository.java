package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.PullRequestFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PullRequestFileRepository extends JpaRepository<PullRequestFile, String> {
    List<PullRequestFile> findByPullRequestId(String pullRequestId);
    Optional<PullRequestFile> findByPullRequestIdAndFilename(String pullRequestId, String filename);
    void deleteByPullRequestId(String pullRequestId);
}
