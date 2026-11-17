package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.repo.PullRequestFileResponse;
import com.aicode.reviewer.dto.repo.PullRequestResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.repository.PullRequestFileRepository;
import com.aicode.reviewer.repository.PullRequestRepository;
import com.aicode.reviewer.repository.ReviewFindingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PullRequestService {

    public PullRequestService(PullRequestRepository pullRequestRepository, PullRequestFileRepository pullRequestFileRepository, ReviewFindingRepository reviewFindingRepository) {
        this.pullRequestRepository = pullRequestRepository;
        this.pullRequestFileRepository = pullRequestFileRepository;
        this.reviewFindingRepository = reviewFindingRepository;
    }


    private final PullRequestRepository pullRequestRepository;
    private final PullRequestFileRepository pullRequestFileRepository;
    private final ReviewFindingRepository reviewFindingRepository;

    public List<PullRequestResponse> getPullRequests(String repoId, String state, String search) {
        List<PullRequest> list;
        if (repoId != null && !repoId.isBlank()) {
            list = state != null && !state.isBlank()
                    ? pullRequestRepository.findByRepositoryIdAndState(repoId, state)
                    : pullRequestRepository.findByRepositoryId(repoId);
        } else if (state != null && !state.isBlank()) {
            list = pullRequestRepository.findByState(state);
        } else {
            list = pullRequestRepository.findAll();
        }

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            list = list.stream()
                    .filter(pr -> pr.getTitle().toLowerCase().contains(q) ||
                                  pr.getRepository().getFullName().toLowerCase().contains(q) ||
                                  pr.getAuthorUsername().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public PullRequestResponse getPullRequestById(String id) {
        PullRequest pr = pullRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pull request not found with id: " + id));
        return mapToResponse(pr);
    }

    public List<PullRequestFileResponse> getPullRequestFiles(String pullRequestId) {
        return pullRequestFileRepository.findByPullRequestId(pullRequestId)
                .stream()
                .map(this::mapFileToResponse)
                .collect(Collectors.toList());
    }

    private PullRequestResponse mapToResponse(PullRequest pr) {
        var findings = reviewFindingRepository.findByPullRequestId(pr.getId());
        int crit = (int) findings.stream().filter(f -> f.getSeverity() == FindingSeverity.CRITICAL).count();
        int high = (int) findings.stream().filter(f -> f.getSeverity() == FindingSeverity.HIGH).count();
        int med = (int) findings.stream().filter(f -> f.getSeverity() == FindingSeverity.MEDIUM).count();
        int low = (int) findings.stream().filter(f -> f.getSeverity() == FindingSeverity.LOW).count();
        int info = (int) findings.stream().filter(f -> f.getSeverity() == FindingSeverity.INFO).count();

        return PullRequestResponse.builder()
                .id(pr.getId())
                .repositoryId(pr.getRepository().getId())
                .repositoryName(pr.getRepository().getFullName())
                .githubPrNumber(pr.getGithubPrNumber())
                .title(pr.getTitle())
                .description(pr.getDescription())
                .authorUsername(pr.getAuthorUsername())
                .authorAvatarUrl(pr.getAuthorAvatarUrl())
                .state(pr.getState())
                .sourceBranch(pr.getSourceBranch())
                .targetBranch(pr.getTargetBranch())
                .htmlUrl(pr.getHtmlUrl())
                .additions(pr.getAdditions())
                .deletions(pr.getDeletions())
                .changedFilesCount(pr.getChangedFilesCount())
                .qualityScore(pr.getQualityScore())
                .securityScore(pr.getSecurityScore())
                .criticalCount(crit)
                .highCount(high)
                .mediumCount(med)
                .lowCount(low)
                .infoCount(info)
                .createdAt(pr.getCreatedAt())
                .updatedAt(pr.getUpdatedAt())
                .build();
    }

    private PullRequestFileResponse mapFileToResponse(PullRequestFile f) {
        return PullRequestFileResponse.builder()
                .id(f.getId())
                .pullRequestId(f.getPullRequest().getId())
                .filename(f.getFilename())
                .oldPath(f.getOldPath())
                .status(f.getStatus())
                .additions(f.getAdditions())
                .deletions(f.getDeletions())
                .patch(f.getPatch())
                .findingsCount(f.getFindingsCount())
                .build();
    }
}
