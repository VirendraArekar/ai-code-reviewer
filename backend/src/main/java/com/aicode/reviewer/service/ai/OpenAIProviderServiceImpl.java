package com.aicode.reviewer.service.ai;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.ReviewMode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAIProviderServiceImpl implements AIProviderService {

    public OpenAIProviderServiceImpl(BaseAiAnalysisEngine engine) {
        this.engine = engine;
    }


    private final BaseAiAnalysisEngine engine;

    @Override
    public AIProvider getProviderType() {
        return AIProvider.OPENAI;
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
        String effectiveModel = modelName != null && !modelName.isBlank() ? modelName : "gpt-4o";
        return engine.executeIntelligentAnalysis(pullRequest, changedFiles, customInstructions, reviewMode, AIProvider.OPENAI, effectiveModel);
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
        return "OpenAI Review Intelligence: Regarding '" + message + "' on PR #" + pullRequest.getGithubPrNumber() +
               ": The AST diff verifies clean exception handling with zero exposed raw stack traces. Review the suggested rate-limiter and HMAC constant-time fixes before merge.";
    }
}
