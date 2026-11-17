package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.RepositoryEntity;
import com.aicode.reviewer.entity.WebhookEvent;
import com.aicode.reviewer.repository.PullRequestRepository;
import com.aicode.reviewer.repository.RepositoryRepository;
import com.aicode.reviewer.repository.WebhookEventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

@Service
public class GitHubWebhookService {

    private static final Logger log = LoggerFactory.getLogger(GitHubWebhookService.class);

    private final RepositoryRepository repositoryRepository;
    private final PullRequestRepository pullRequestRepository;
    private final WebhookEventRepository webhookEventRepository;
    private final AiReviewService aiReviewService;
    private final GitHubService gitHubService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.github.webhook-secret:development_webhook_secret_key_123456}")
    private String webhookSecret;

    public GitHubWebhookService(
            RepositoryRepository repositoryRepository,
            PullRequestRepository pullRequestRepository,
            WebhookEventRepository webhookEventRepository,
            @Lazy AiReviewService aiReviewService,
            @Lazy GitHubService gitHubService
    ) {
        this.repositoryRepository = repositoryRepository;
        this.pullRequestRepository = pullRequestRepository;
        this.webhookEventRepository = webhookEventRepository;
        this.aiReviewService = aiReviewService;
        this.gitHubService = gitHubService;
    }

    public boolean verifySignature(String payload, String signatureHeader) {
        if (signatureHeader == null || !signatureHeader.startsWith("sha256=")) {
            return false;
        }
        try {
            String expectedHash = signatureHeader.substring(7);
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hmac) {
                sb.append(String.format("%02x", b));
            }
            String computedHash = sb.toString();

            // Constant-time check to prevent timing attacks
            return MessageDigest.isEqual(
                    expectedHash.getBytes(StandardCharsets.UTF_8),
                    computedHash.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            log.error("Failed to verify HMAC signature", e);
            return false;
        }
    }

    @Transactional
    public void processWebhookEvent(String eventType, String deliveryId, String payload) {
        if (webhookEventRepository.existsByDeliveryId(deliveryId)) {
            log.info("Webhook event already processed (idempotency check): {}", deliveryId);
            return;
        }

        WebhookEvent event = WebhookEvent.builder()
                .id(UUID.randomUUID().toString())
                .githubEvent(eventType)
                .deliveryId(deliveryId)
                .payload(payload)
                .processed(false)
                .build();
        webhookEventRepository.save(event);

        try {
            if ("pull_request".equalsIgnoreCase(eventType)) {
                handlePullRequestEvent(payload);
            }
            event.setProcessed(true);
            webhookEventRepository.save(event);
        } catch (Exception e) {
            log.error("Error processing webhook payload", e);
            event.setErrorMessage(e.getMessage());
            webhookEventRepository.save(event);
        }
    }

    private void handlePullRequestEvent(String payload) throws Exception {
        JsonNode root = objectMapper.readTree(payload);
        String action = root.path("action").asText();
        JsonNode prNode = root.path("pull_request");
        JsonNode repoNode = root.path("repository");

        String fullName = repoNode.path("full_name").asText();
        RepositoryEntity repository = repositoryRepository.findByFullName(fullName).orElse(null);

        if (repository == null) {
            log.warn("Repository {} not connected in workspace. Skipping automated review.", fullName);
            return;
        }

        int prNumber = prNode.path("number").asInt(1);
        String title = prNode.path("title").asText("Untitled PR");
        String body = prNode.path("body").asText("");
        String state = prNode.path("state").asText("OPEN").toUpperCase();
        String author = prNode.path("user").path("login").asText("unknown");
        String avatarUrl = prNode.path("user").path("avatar_url").asText("");
        String htmlUrl = prNode.path("html_url").asText(repository.getHtmlUrl() + "/pull/" + prNumber);
        String sourceBranch = prNode.path("head").path("ref").asText("feature");
        String targetBranch = prNode.path("base").path("ref").asText(repository.getDefaultBranch());
        int additions = prNode.path("additions").asInt(0);
        int deletions = prNode.path("deletions").asInt(0);
        int changedFiles = prNode.path("changed_files").asInt(0);

        PullRequest pr = pullRequestRepository.findByRepositoryIdAndGithubPrNumber(repository.getId(), prNumber)
                .orElse(null);

        if (pr == null) {
            pr = PullRequest.builder()
                    .id(UUID.randomUUID().toString())
                    .repository(repository)
                    .githubPrNumber(prNumber)
                    .title(title)
                    .description(body)
                    .authorUsername(author)
                    .authorAvatarUrl(avatarUrl)
                    .state(state)
                    .sourceBranch(sourceBranch)
                    .targetBranch(targetBranch)
                    .htmlUrl(htmlUrl)
                    .additions(additions)
                    .deletions(deletions)
                    .changedFilesCount(changedFiles)
                    .build();
        } else {
            pr.setTitle(title);
            pr.setDescription(body);
            pr.setState(state);
            pr.setAdditions(additions);
            pr.setDeletions(deletions);
            pr.setChangedFilesCount(changedFiles);
        }
        PullRequest savedPr = pullRequestRepository.save(pr);

        String[] parts = fullName.split("/");
        String owner = parts.length > 0 ? parts[0] : repository.getOwnerLogin();
        String repoName = parts.length > 1 ? parts[1] : repository.getName();

        // Fetch real files and patches from GitHub API
        gitHubService.fetchAndSaveFilesForPr(savedPr, owner, repoName, prNumber);

        if (("opened".equals(action) || "synchronize".equals(action) || "reopened".equals(action)) && repository.isAutoReviewEnabled()) {
            triggerAsyncReview(savedPr.getId(), owner, repoName, prNumber, repository.isInlineCommentsEnabled());
        }
    }

    @Async
    public void triggerAsyncReview(String pullRequestId, String owner, String repoName, int prNumber, boolean postComment) {
        try {
            log.info("Triggering async AI review for PR id: {}", pullRequestId);
            AiReviewResponse reviewResponse = aiReviewService.executeReview(pullRequestId, null);

            if (postComment && reviewResponse != null) {
                String comment = "### 🤖 AI Code Reviewer Analysis\n\n" +
                        "**Quality Score**: " + reviewResponse.getQualityScore() + "/100 | " +
                        "**Security Score**: " + reviewResponse.getSecurityScore() + "/100\n\n" +
                        reviewResponse.getSummary() + "\n\n" +
                        "Found **" + reviewResponse.getFindings().size() + "** issue(s).";
                gitHubService.postReviewComment(owner, repoName, prNumber, comment);
            }
        } catch (Exception e) {
            log.error("Async review execution failed for PR: {}", pullRequestId, e);
        }
    }
}
