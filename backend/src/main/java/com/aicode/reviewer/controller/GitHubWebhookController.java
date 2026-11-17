package com.aicode.reviewer.controller;

import com.aicode.reviewer.service.GitHubWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/webhooks/github")
@Tag(name = "GitHub Webhooks", description = "Secure HMAC endpoint receiving GitHub pull request and check events")
public class GitHubWebhookController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GitHubWebhookController.class);

    public GitHubWebhookController(GitHubWebhookService gitHubWebhookService) {
        this.gitHubWebhookService = gitHubWebhookService;
    }


    private final GitHubWebhookService gitHubWebhookService;

    @PostMapping
    @Operation(summary = "Handle GitHub webhook deliveries with HMAC-SHA256 signature verification")
    public ResponseEntity<?> handleWebhook(
            @RequestHeader(value = "X-GitHub-Event", defaultValue = "ping") String eventType,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String deliveryId,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload
    ) {
        log.info("Received GitHub Webhook event: {} (delivery: {})", eventType, deliveryId);

        // Verify signature if provided or in non-local environments
        if (signature != null && !gitHubWebhookService.verifySignature(payload, signature)) {
            log.warn("Invalid HMAC signature received for GitHub webhook delivery: {}", deliveryId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid HMAC signature"));
        }

        String effectiveDeliveryId = deliveryId != null ? deliveryId : UUID.randomUUID().toString();
        gitHubWebhookService.processWebhookEvent(eventType, effectiveDeliveryId, payload);

        return ResponseEntity.ok(Map.of("status", "accepted", "event", eventType));
    }
}
