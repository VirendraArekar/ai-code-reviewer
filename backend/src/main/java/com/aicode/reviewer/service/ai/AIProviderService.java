package com.aicode.reviewer.service.ai;

import com.aicode.reviewer.dto.review.AiReviewResponse;
import com.aicode.reviewer.entity.PullRequest;
import com.aicode.reviewer.entity.PullRequestFile;
import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.entity.enums.ReviewMode;

import java.util.List;

public interface AIProviderService {
    AIProvider getProviderType();

    AiReviewResponse analyzeDiff(
            PullRequest pullRequest,
            List<PullRequestFile> changedFiles,
            String customInstructions,
            ReviewMode reviewMode,
            String apiKey,
            String modelName,
            String baseUrl
    );

    String chat(
            PullRequest pullRequest,
            List<PullRequestFile> changedFiles,
            String message,
            String apiKey,
            String modelName,
            String baseUrl
    );
}
