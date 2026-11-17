package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.repo.*;
import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.service.AiReviewService;
import com.aicode.reviewer.service.GitHubService;
import com.aicode.reviewer.service.PullRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pull-requests")
@Tag(name = "Pull Request Management", description = "Endpoints for pull requests, diff viewer, AI triggers, and PR chat")
public class PullRequestController {

    public PullRequestController(PullRequestService pullRequestService, AiReviewService aiReviewService, GitHubService gitHubService) {
        this.pullRequestService = pullRequestService;
        this.aiReviewService = aiReviewService;
        this.gitHubService = gitHubService;
    }

    private final PullRequestService pullRequestService;
    private final AiReviewService aiReviewService;
    private final GitHubService gitHubService;

    @GetMapping
    @Operation(summary = "List pull requests with optional repo, status, and search filters")
    public ResponseEntity<List<PullRequestResponse>> getPullRequests(
            @RequestParam(required = false) String repoId,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(pullRequestService.getPullRequests(repoId, state, search));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pull request detail by ID")
    public ResponseEntity<PullRequestResponse> getPullRequestById(@PathVariable String id) {
        return ResponseEntity.ok(pullRequestService.getPullRequestById(id));
    }

    @GetMapping("/{id}/files")
    @Operation(summary = "Get changed files and diff patches for a pull request")
    public ResponseEntity<List<PullRequestFileResponse>> getPullRequestFiles(@PathVariable String id) {
        return ResponseEntity.ok(pullRequestService.getPullRequestFiles(id));
    }

    @PostMapping("/{id}/sync")
    @Operation(summary = "Sync pull request diff and files from GitHub API and re-run analysis")
    public ResponseEntity<Map<String, Object>> syncPullRequest(@PathVariable String id) {
        gitHubService.syncPullRequest(id);
        return ResponseEntity.ok(Map.of("status", "synced"));
    }

    @PostMapping("/{id}/review")
    @Operation(summary = "Trigger autonomous AI code review on pull request")
    public ResponseEntity<AiReviewResponse> triggerReview(
            @PathVariable String id,
            @RequestBody(required = false) ReviewTriggerRequest request
    ) {
        var mode = request != null ? request.getMode() : null;
        return ResponseEntity.ok(aiReviewService.executeReview(id, mode));
    }

    @PostMapping("/{id}/chat")
    @Operation(summary = "Chat contextually with AI assistant regarding pull request diff")
    public ResponseEntity<PrChatResponse> chatWithPr(
            @PathVariable String id,
            @Valid @RequestBody PrChatRequest request
    ) {
        String answer = aiReviewService.chatWithPr(id, request.getMessage());
        return ResponseEntity.ok(PrChatResponse.builder().response(answer).build());
    }
}
