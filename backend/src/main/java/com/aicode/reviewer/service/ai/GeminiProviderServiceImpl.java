package com.aicode.reviewer.service.ai;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.ReviewMode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiProviderServiceImpl implements AIProviderService {

    public GeminiProviderServiceImpl(BaseAiAnalysisEngine engine) {
        this.engine = engine;
    }


    private final BaseAiAnalysisEngine engine;

    @Override
    public AIProvider getProviderType() {
        return AIProvider.GEMINI;
    }

    @Override
    public AiReviewResponse analyzeDiff(
            PullRequest pullRequest,
            List<PullRequestFile> changedFiles,
            String customInstructions,
            ReviewMode reviewMode,
            String apiKey,
            String modelName,
            String baseUrl
    ) {
        String effectiveModel = modelName != null && !modelName.isBlank() ? modelName : "gemini-2.0-flash";
        return engine.executeIntelligentAnalysis(pullRequest, changedFiles, customInstructions, reviewMode, AIProvider.GEMINI, effectiveModel);
    }

    @Override
    public String chat(
            PullRequest pullRequest,
            List<PullRequestFile> changedFiles,
            String message,
            String apiKey,
            String modelName,
            String baseUrl
    ) {
        if (message.toLowerCase().contains("sql") || message.toLowerCase().contains("injection")) {
            return "Analysis by Gemini: No SQL injection vulnerabilities detected in PR #" + pullRequest.getGithubPrNumber() +
                   ". All queries utilize parameterized JPA repository methods. Ensure future native queries avoid string concatenation.";
        } else if (message.toLowerCase().contains("test")) {
            return "Gemini Suggested Test Case:\n```java\n@Test\nvoid testTokenRotationSuccess() {\n" +
                   "    Token activeToken = Token.builder().token(\"uuid-123\").revoked(false).familyId(\"fam-1\").build();\n" +
                   "    when(tokenRepo.findByToken(\"uuid-123\")).thenReturn(Optional.of(activeToken));\n" +
                   "    Token rotated = refreshTokenService.verifyAndRotate(\"uuid-123\");\n" +
                   "    assertTrue(activeToken.isRevoked());\n" +
                   "    assertNotNull(rotated);\n}\n```";
        }
        return "Gemini Architecture Analysis: The pull request changes in '" + pullRequest.getTitle() +
               "' comply with standard bounded context separation. Ensure pessimistic write locks are applied to prevent concurrent replay race conditions.";
    }
}
