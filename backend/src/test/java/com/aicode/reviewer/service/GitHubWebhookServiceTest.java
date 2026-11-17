package com.aicode.reviewer.service;

import com.aicode.reviewer.repository.PullRequestFileRepository;
import com.aicode.reviewer.repository.PullRequestRepository;
import com.aicode.reviewer.repository.RepositoryRepository;
import com.aicode.reviewer.repository.WebhookEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GitHubWebhookServiceTest {

    private GitHubWebhookService service;
    private final String secret = "test_webhook_secret_key_123";

    @BeforeEach
    void setUp() {
        RepositoryRepository repoRepo = Mockito.mock(RepositoryRepository.class);
        PullRequestRepository prRepo = Mockito.mock(PullRequestRepository.class);
        PullRequestFileRepository fileRepo = Mockito.mock(PullRequestFileRepository.class);
        WebhookEventRepository eventRepo = Mockito.mock(WebhookEventRepository.class);
        AiReviewService aiReviewService = Mockito.mock(AiReviewService.class);

        service = new GitHubWebhookService(repoRepo, prRepo, fileRepo, eventRepo, aiReviewService);
        ReflectionTestUtils.setField(service, "webhookSecret", secret);
    }

    @Test
    void shouldVerifyValidHmacSha256Signature() throws Exception {
        String payload = "{\"action\":\"opened\",\"number\":42}";

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hmac) {
            sb.append(String.format("%02x", b));
        }
        String validHeader = "sha256=" + sb.toString();

        assertTrue(service.verifySignature(payload, validHeader));
    }

    @Test
    void shouldRejectTamperedPayload() {
        String payload = "{\"action\":\"opened\",\"number\":42}";
        String tamperedPayload = "{\"action\":\"opened\",\"number\":43}";
        String signatureHeader = "sha256=1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";

        assertFalse(service.verifySignature(tamperedPayload, signatureHeader));
        assertFalse(service.verifySignature(payload, "invalid_prefix_signature"));
        assertFalse(service.verifySignature(payload, null));
    }
}
