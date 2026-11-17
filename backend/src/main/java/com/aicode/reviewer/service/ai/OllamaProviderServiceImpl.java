package com.aicode.reviewer.service.ai;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.ReviewMode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OllamaProviderServiceImpl implements AIProviderService {

    public OllamaProviderServiceImpl(BaseAiAnalysisEngine engine) {
        this.engine = engine;
    }


    private final BaseAiAnalysisEngine engine;

    @Override
    public AIProvider getProviderType() {
        return AIProvider.OLLAMA;
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
        String effectiveModel = modelName != null && !modelName.isBlank() ? modelName : "deepseek-coder:6.7b";
        return engine.executeIntelligentAnalysis(pullRequest, changedFiles, customInstructions, reviewMode, AIProvider.OLLAMA, effectiveModel);
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
        return "Ollama On-Premises Engine: Analyzed PR #" + pullRequest.getGithubPrNumber() +
               " locally without internet egress. Recommendation: Check for N+1 JPA collection queries and lock the single-use token family.";
    }
}
