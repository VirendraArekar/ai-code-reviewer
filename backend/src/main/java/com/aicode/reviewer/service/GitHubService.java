package com.aicode.reviewer.service;

import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.RepositoryEntity;
import com.aicode.reviewer.repository.PullRequestFileRepository;
import com.aicode.reviewer.repository.PullRequestRepository;
import com.aicode.reviewer.repository.RepositoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GitHubService {

    private static final Logger log = LoggerFactory.getLogger(GitHubService.class);

    private final RepositoryRepository repositoryRepository;
    private final PullRequestRepository pullRequestRepository;
    private final PullRequestFileRepository pullRequestFileRepository;
    private final AiReviewService aiReviewService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient;

    @Value("${app.github.token:}")
    private String githubToken;

    @Value("${app.github.client-id:}")
    private String clientId;

    @Value("${app.github.client-secret:}")
    private String clientSecret;

    @Value("${app.github.webhook-secret:development_webhook_secret_key_123456}")
    private String webhookSecret;

    @Value("${app.github.app-id:}")
    private String appId;

    @Value("${app.github.app-name:ai-code-reviewer}")
    private String appName;

    public GitHubService(
            RepositoryRepository repositoryRepository,
            PullRequestRepository pullRequestRepository,
            PullRequestFileRepository pullRequestFileRepository,
            @Lazy AiReviewService aiReviewService
    ) {
        this.repositoryRepository = repositoryRepository;
        this.pullRequestRepository = pullRequestRepository;
        this.pullRequestFileRepository = pullRequestFileRepository;
        this.aiReviewService = aiReviewService;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    private HttpRequest.Builder createRequestBuilder(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("User-Agent", "AICodeReviewer-ProductionApp")
                .timeout(Duration.ofSeconds(15));

        String token = getEffectiveToken();
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token.trim());
        }
        return builder;
    }

    public String getEffectiveToken() {
        if (githubToken != null && !githubToken.isBlank()) {
            return githubToken;
        }
        String envToken = System.getenv("GITHUB_TOKEN");
        if (envToken != null && !envToken.isBlank()) {
            return envToken;
        }
        return null;
    }

    public Map<String, Object> fetchRepositoryDetails(String owner, String repo) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo;
        log.info("Fetching repository metadata from GitHub API: {}", url);
        try {
            HttpRequest request = createRequestBuilder(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new IllegalArgumentException("Repository '" + owner + "/" + repo + "' not found on GitHub. Check owner/repo name or permissions.");
            }
            if (response.statusCode() == 401 || response.statusCode() == 403) {
                log.warn("GitHub API rate limit or auth requirement encountered: HTTP {}", response.statusCode());
                throw new IllegalStateException("GitHub API authorization error (HTTP " + response.statusCode() + "). Please provide a valid GITHUB_TOKEN in .env or Settings.");
            }
            if (response.statusCode() != 200) {
                throw new IllegalStateException("GitHub API returned HTTP " + response.statusCode() + ": " + response.body());
            }

            JsonNode root = objectMapper.readTree(response.body());
            Map<String, Object> map = new HashMap<>();
            map.put("githubRepoId", root.path("id").asLong());
            map.put("name", root.path("name").asText(repo));
            map.put("fullName", root.path("full_name").asText(owner + "/" + repo));
            map.put("ownerLogin", root.path("owner").path("login").asText(owner));
            map.put("defaultBranch", root.path("default_branch").asText("main"));
            map.put("privateRepo", root.path("private").asBoolean(false));
            map.put("htmlUrl", root.path("html_url").asText("https://github.com/" + owner + "/" + repo));
            map.put("primaryLanguage", root.path("language").asText("Java"));
            map.put("description", root.path("description").asText(""));
            return map;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to connect to GitHub API for repo {}/{}", owner, repo, e);
            throw new RuntimeException("GitHub connection failure: " + e.getMessage(), e);
        }
    }

    public List<JsonNode> fetchPullRequests(String owner, String repo, String state) {
        String effectiveState = (state != null && !state.isBlank()) ? state : "all";
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/pulls?state=" + effectiveState + "&per_page=30";
        log.info("Fetching pull requests from GitHub API: {}", url);
        try {
            HttpRequest request = createRequestBuilder(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("GitHub API error fetching pull requests: HTTP {} - {}", response.statusCode(), response.body());
                return Collections.emptyList();
            }

            JsonNode arrayNode = objectMapper.readTree(response.body());
            List<JsonNode> prs = new ArrayList<>();
            if (arrayNode.isArray()) {
                for (JsonNode item : arrayNode) {
                    prs.add(item);
                }
            }
            return prs;
        } catch (Exception e) {
            log.error("Failed to fetch pull requests from GitHub API for {}/{}", owner, repo, e);
            return Collections.emptyList();
        }
    }

    public List<JsonNode> fetchPullRequestFiles(String owner, String repo, int prNumber) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + prNumber + "/files?per_page=50";
        log.info("Fetching changed files and patches from GitHub API: {}", url);
        try {
            HttpRequest request = createRequestBuilder(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("GitHub API error fetching files for PR #{}: HTTP {}", prNumber, response.statusCode());
                return Collections.emptyList();
            }

            JsonNode arrayNode = objectMapper.readTree(response.body());
            List<JsonNode> files = new ArrayList<>();
            if (arrayNode.isArray()) {
                for (JsonNode item : arrayNode) {
                    files.add(item);
                }
            }
            return files;
        } catch (Exception e) {
            log.error("Failed to fetch files from GitHub API for {}/{} PR #{}", owner, repo, prNumber, e);
            return Collections.emptyList();
        }
    }

    public boolean postReviewComment(String owner, String repo, int prNumber, String commentBody) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues/" + prNumber + "/comments";
        log.info("Publishing AI code review comment to GitHub PR #{}: {}", prNumber, url);
        try {
            String jsonPayload = objectMapper.writeValueAsString(Map.of("body", commentBody));
            HttpRequest request = createRequestBuilder(url)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                log.info("Successfully posted review comment to GitHub PR #{}", prNumber);
                return true;
            } else {
                log.warn("GitHub API refused comment post: HTTP {} - {}", response.statusCode(), response.body());
                return false;
            }
        } catch (Exception e) {
            log.error("Error posting review comment to GitHub PR #{}", prNumber, e);
            return false;
        }
    }

    @Transactional
    public int syncRepositoryPullRequests(String repositoryId) {
        RepositoryEntity repo = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found: " + repositoryId));

        String fullName = repo.getFullName();
        String[] parts = fullName.split("/");
        String owner = parts.length > 0 ? parts[0] : repo.getOwnerLogin();
        String repoName = parts.length > 1 ? parts[1] : repo.getName();

        List<JsonNode> prNodes = fetchPullRequests(owner, repoName, "all");
        int count = 0;

        for (JsonNode node : prNodes) {
            int prNumber = node.path("number").asInt();
            String title = node.path("title").asText("Untitled PR");
            String body = node.path("body").asText("");
            String state = node.path("state").asText("OPEN").toUpperCase();
            String author = node.path("user").path("login").asText("unknown");
            String avatarUrl = node.path("user").path("avatar_url").asText("");
            String htmlUrl = node.path("html_url").asText(repo.getHtmlUrl() + "/pull/" + prNumber);
            String sourceBranch = node.path("head").path("ref").asText("feature");
            String targetBranch = node.path("base").path("ref").asText(repo.getDefaultBranch());

            PullRequest pr = pullRequestRepository.findByRepositoryIdAndGithubPrNumber(repo.getId(), prNumber)
                    .orElse(null);

            if (pr == null) {
                pr = PullRequest.builder()
                        .id(UUID.randomUUID().toString())
                        .repository(repo)
                        .githubPrNumber(prNumber)
                        .title(title)
                        .description(body)
                        .authorUsername(author)
                        .authorAvatarUrl(avatarUrl)
                        .state(state)
                        .sourceBranch(sourceBranch)
                        .targetBranch(targetBranch)
                        .htmlUrl(htmlUrl)
                        .additions(0)
                        .deletions(0)
                        .changedFilesCount(0)
                        .build();
            } else {
                pr.setTitle(title);
                pr.setDescription(body);
                pr.setState(state);
                pr.setSourceBranch(sourceBranch);
                pr.setTargetBranch(targetBranch);
            }

            PullRequest savedPr = pullRequestRepository.save(pr);

            // Fetch and save real changed files & patch diffs
            fetchAndSaveFilesForPr(savedPr, owner, repoName, prNumber);

            // If auto-review enabled and PR is OPEN, trigger intelligent AI review
            if (repo.isAutoReviewEnabled() && "OPEN".equalsIgnoreCase(state)) {
                try {
                    aiReviewService.executeReview(savedPr.getId(), repo.getReviewMode());
                } catch (Exception e) {
                    log.error("Automatic AI review execution failed for PR #{}: {}", prNumber, e.getMessage());
                }
            }

            count++;
        }

        repo.setLastReviewAt(LocalDateTime.now());
        repositoryRepository.save(repo);
        return count;
    }

    @Transactional
    public void fetchAndSaveFilesForPr(PullRequest pr, String owner, String repoName, int prNumber) {
        List<JsonNode> fileNodes = fetchPullRequestFiles(owner, repoName, prNumber);
        int totalAdditions = 0;
        int totalDeletions = 0;

        for (JsonNode fNode : fileNodes) {
            String filename = fNode.path("filename").asText();
            String status = fNode.path("status").asText("modified").toUpperCase();
            int add = fNode.path("additions").asInt(0);
            int del = fNode.path("deletions").asInt(0);
            String patch = fNode.path("patch").asText("");

            totalAdditions += add;
            totalDeletions += del;

            PullRequestFile file = pullRequestFileRepository.findByPullRequestIdAndFilename(pr.getId(), filename)
                    .orElse(null);

            if (file == null) {
                file = PullRequestFile.builder()
                        .id(UUID.randomUUID().toString())
                        .pullRequest(pr)
                        .filename(filename)
                        .status(status)
                        .additions(add)
                        .deletions(del)
                        .patch(patch)
                        .findingsCount(0)
                        .build();
            } else {
                file.setStatus(status);
                file.setAdditions(add);
                file.setDeletions(del);
                file.setPatch(patch);
            }
            pullRequestFileRepository.save(file);
        }

        pr.setAdditions(totalAdditions);
        pr.setDeletions(totalDeletions);
        pr.setChangedFilesCount(fileNodes.size());
        pullRequestRepository.save(pr);
    }

    @Transactional
    public void syncPullRequest(String pullRequestId) {
        PullRequest pr = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Pull request not found: " + pullRequestId));

        RepositoryEntity repo = pr.getRepository();
        String[] parts = repo.getFullName().split("/");
        String owner = parts.length > 0 ? parts[0] : repo.getOwnerLogin();
        String repoName = parts.length > 1 ? parts[1] : repo.getName();

        fetchAndSaveFilesForPr(pr, owner, repoName, pr.getGithubPrNumber());
        aiReviewService.executeReview(pr.getId(), repo.getReviewMode());
    }

    public Map<String, Object> getAppConfig() {
        return Map.of(
                "appId", appId != null ? appId : "",
                "appName", appName != null ? appName : "ai-code-reviewer",
                "clientId", clientId != null ? clientId : "",
                "hasClientSecret", clientSecret != null && !clientSecret.isBlank(),
                "hasToken", getEffectiveToken() != null && !getEffectiveToken().isBlank(),
                "webhookSecretConfigured", webhookSecret != null && !webhookSecret.isBlank(),
                "installUrl", "https://github.com/apps/" + appName,
                "webhookUrl", "/api/v1/webhooks/github"
        );
    }

    public Map<String, Object> generateAppManifest(String callbackUrl, String webhookUrl) {
        Map<String, Object> hookAttributes = new HashMap<>();
        hookAttributes.put("url", webhookUrl);
        hookAttributes.put("active", true);

        Map<String, String> permissions = new HashMap<>();
        permissions.put("pull_requests", "write");
        permissions.put("issues", "write");
        permissions.put("contents", "read");
        permissions.put("checks", "write");
        permissions.put("metadata", "read");

        List<String> defaultEvents = List.of(
                "pull_request",
                "pull_request_review",
                "check_run",
                "check_suite"
        );

        Map<String, Object> manifest = new HashMap<>();
        manifest.put("name", "AI Code Reviewer - " + UUID.randomUUID().toString().substring(0, 8));
        manifest.put("url", "https://reviewai.dev");
        manifest.put("hook_attributes", hookAttributes);
        manifest.put("redirect_url", callbackUrl);
        manifest.put("public", false);
        manifest.put("default_permissions", permissions);
        manifest.put("default_events", defaultEvents);

        return manifest;
    }
}
