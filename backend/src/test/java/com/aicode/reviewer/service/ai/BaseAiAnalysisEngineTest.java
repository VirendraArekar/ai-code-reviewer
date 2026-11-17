package com.aicode.reviewer.service.ai;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.dto.review.ReviewFindingResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.RepositoryEntity;
import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.ReviewMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseAiAnalysisEngineTest {

    private BaseAiAnalysisEngine engine;
    private PullRequest testPr;

    @BeforeEach
    void setUp() {
        engine = new BaseAiAnalysisEngine();
        RepositoryEntity repo = RepositoryEntity.builder()
                .id("repo-1")
                .fullName("company/payment-service")
                .build();

        testPr = PullRequest.builder()
                .id("pr-42")
                .repository(repo)
                .githubPrNumber(42)
                .title("Add webhook verification handler")
                .description("Verifies incoming webhook payload signature")
                .build();
    }

    @Test
    void testDetectTimingAttackInSignatureComparison() {
        String patch = """
                @@ -10,6 +10,12 @@
                 public boolean verify(String actualSignature, String expectedSignature) {
                +    if (expectedSignature.equals(actualSignature)) {
                +        return true;
                +    }
                +    return false;
                 }
                """;

        PullRequestFile file = PullRequestFile.builder()
                .id("file-1")
                .pullRequest(testPr)
                .filename("src/main/java/com/company/webhook/WebhookSignatureVerifier.java")
                .patch(patch)
                .build();

        AiReviewResponse review = engine.executeIntelligentAnalysis(
                testPr,
                List.of(file),
                "Enforce constant-time algorithms",
                ReviewMode.SECURITY_FOCUSED,
                AIProvider.GEMINI,
                "gemini-2.0-flash"
        );

        assertNotNull(review);
        assertFalse(review.getFindings().isEmpty(), "Should detect at least 1 finding");

        ReviewFindingResponse finding = review.getFindings().stream()
                .filter(f -> f.getSeverity() == FindingSeverity.CRITICAL)
                .findFirst()
                .orElse(null);

        assertNotNull(finding, "Should flag timing attack with CRITICAL severity");
        assertEquals(FindingCategory.SECURITY, finding.getCategory());
        assertTrue(finding.getTitle().contains("Timing Attack"));
        assertTrue(review.getSecurityScore() < 100, "Security score should reflect critical penalty");
    }

    @Test
    void testCleanDiffYieldsHighScore() {
        String patch = """
                @@ -1,4 +1,4 @@
                -int count = 0;
                +final int count = 0;
                """;

        PullRequestFile file = PullRequestFile.builder()
                .id("file-2")
                .pullRequest(testPr)
                .filename("src/main/java/com/company/util/Counter.java")
                .patch(patch)
                .build();

        AiReviewResponse review = engine.executeIntelligentAnalysis(
                testPr,
                List.of(file),
                null,
                ReviewMode.QUICK,
                AIProvider.OPENAI,
                "gpt-4o-mini"
        );

        assertNotNull(review);
        assertEquals(100, review.getSecurityScore());
        assertEquals(100, review.getQualityScore());
    }

    @Test
    void testBuildPromptSanitizesInput() {
        testPr.setTitle("Test PR ```with markdown injection```");
        String prompt = engine.buildPrompt(testPr, List.of(), "Custom rules", ReviewMode.STANDARD);

        assertNotNull(prompt);
        assertFalse(prompt.contains("```with markdown injection```"), "Backticks should be sanitized to avoid prompt breakouts");
        assertTrue(prompt.contains("'''with markdown injection'''"));
    }
}
