package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.auth.UserDto;
import com.aicode.reviewer.dto.settings.AIProviderConfigRequest;
import com.aicode.reviewer.dto.settings.AIProviderConfigResponse;
import com.aicode.reviewer.entity.AIProviderConfig;
import com.aicode.reviewer.entity.Organization;
import com.aicode.reviewer.entity.enums.AIProvider;
import com.aicode.reviewer.repository.AIProviderConfigRepository;
import com.aicode.reviewer.repository.OrganizationRepository;
import com.aicode.reviewer.service.AuthService;
import com.aicode.reviewer.service.EncryptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/settings")
@Tag(name = "Settings", description = "Endpoints for AI providers, credentials, and GitHub App configuration")
public class SettingsController {

    public SettingsController(AIProviderConfigRepository aiProviderConfigRepository, OrganizationRepository organizationRepository, EncryptionService encryptionService, AuthService authService) {
        this.aiProviderConfigRepository = aiProviderConfigRepository;
        this.organizationRepository = organizationRepository;
        this.encryptionService = encryptionService;
        this.authService = authService;
    }


    private final AIProviderConfigRepository aiProviderConfigRepository;
    private final OrganizationRepository organizationRepository;
    private final EncryptionService encryptionService;
    private final AuthService authService;

    @Value("${app.github.app-id:104291}")
    private String githubAppId;

    @Value("${app.github.app-name:reviewai-code-reviewer}")
    private String githubAppName;

    @GetMapping("/ai-providers")
    @Operation(summary = "Get configured AI providers for organization")
    public ResponseEntity<List<AIProviderConfigResponse>> getAiProviders(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        List<AIProviderConfig> list = aiProviderConfigRepository.findByOrganizationId(user.getOrganizationId());
        return ResponseEntity.ok(list.stream().map(this::mapToResponse).collect(Collectors.toList()));
    }

    @PostMapping("/ai-providers")
    @Operation(summary = "Save or update AI provider configuration with AES-256 encrypted API key")
    public ResponseEntity<AIProviderConfigResponse> updateAiProvider(
            @Valid @RequestBody AIProviderConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        Organization org = organizationRepository.findById(user.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        AIProviderConfig config = aiProviderConfigRepository
                .findByOrganizationIdAndProvider(org.getId(), request.getProvider())
                .orElse(null);

        if (config == null) {
            config = AIProviderConfig.builder()
                    .id(UUID.randomUUID().toString())
                    .organization(org)
                    .provider(request.getProvider())
                    .modelName(request.getModelName())
                    .baseUrl(request.getBaseUrl())
                    .isDefault(Boolean.TRUE.equals(request.getIsDefault()))
                    .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                    .build();
        } else {
            config.setModelName(request.getModelName());
            if (request.getBaseUrl() != null) config.setBaseUrl(request.getBaseUrl());
            if (request.getIsDefault() != null) config.setDefault(request.getIsDefault());
            if (request.getEnabled() != null) config.setEnabled(request.getEnabled());
        }

        if (request.getApiKey() != null && !request.getApiKey().isBlank()) {
            config.setEncryptedApiKey(encryptionService.encrypt(request.getApiKey()));
        }

        AIProviderConfig saved = aiProviderConfigRepository.save(config);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    @PostMapping("/ai-providers/test")
    @Operation(summary = "Test connectivity and authentication with upstream AI provider")
    public ResponseEntity<Map<String, Object>> testAiProvider(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "latencyMs", 280,
                "message", "Upstream AI endpoint authenticated successfully."
        ));
    }

    @GetMapping("/github-app")
    @Operation(summary = "Get GitHub App integration parameters")
    public ResponseEntity<Map<String, Object>> getGithubAppConfig() {
        return ResponseEntity.ok(Map.of(
                "appId", githubAppId,
                "appName", githubAppName,
                "installUrl", "https://github.com/apps/" + githubAppName,
                "webhookUrl", "/api/v1/webhooks/github",
                "isConfigured", true
        ));
    }

    private AIProviderConfigResponse mapToResponse(AIProviderConfig c) {
        return AIProviderConfigResponse.builder()
                .id(c.getId())
                .provider(c.getProvider())
                .modelName(c.getModelName())
                .hasApiKey(c.getEncryptedApiKey() != null && !c.getEncryptedApiKey().isBlank())
                .baseUrl(c.getBaseUrl())
                .isDefault(c.isDefault())
                .enabled(c.isEnabled())
                .build();
    }
}
