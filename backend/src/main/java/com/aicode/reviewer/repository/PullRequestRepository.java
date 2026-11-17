package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PullRequestRepository extends JpaRepository<PullRequest, String> {
    List<PullRequest> findByRepositoryId(String repositoryId);
    List<PullRequest> findByRepositoryIdAndState(String repositoryId, String state);
    Optional<PullRequest> findByRepositoryIdAndGithubPrNumber(String repositoryId, int githubPrNumber);
    List<PullRequest> findByState(String state);
}
