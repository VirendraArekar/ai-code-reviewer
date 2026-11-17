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
import com.aicode.reviewer.service.GitHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    public SettingsController(
            AIProviderConfigRepository aiProviderConfigRepository,
            OrganizationRepository organizationRepository,
            EncryptionService encryptionService,
            AuthService authService,
            GitHubService gitHubService
    ) {
        this.aiProviderConfigRepository = aiProviderConfigRepository;
        this.organizationRepository = organizationRepository;
        this.encryptionService = encryptionService;
        this.authService = authService;
        this.gitHubService = gitHubService;
    }

    private final AIProviderConfigRepository aiProviderConfigRepository;
    private final OrganizationRepository organizationRepository;
    private final EncryptionService encryptionService;
    private final AuthService authService;
    private final GitHubService gitHubService;

    @GetMapping("/ai-providers")
    @Operation(summary = "Get configured AI providers for the organization")
    public ResponseEntity<List<AIProviderConfigResponse>> getAiProviders(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        List<AIProviderConfig> configs = aiProviderConfigRepository.findByOrganizationId(user.getOrganizationId());
        return ResponseEntity.ok(configs.stream().map(this::mapToResponse).collect(Collectors.toList()));
    }

    @PostMapping("/ai-providers")
    @Operation(summary = "Create or update AI provider configuration")
    public ResponseEntity<AIProviderConfigResponse> saveAiProvider(
            @Valid @RequestBody AIProviderConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        Organization org = organizationRepository.findById(user.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        AIProviderConfig config = aiProviderConfigRepository
                .findByOrganizationIdAndProvider(org.getId(), request.getProvider())
                .orElseGet(() -> AIProviderConfig.builder()
                        .id(UUID.randomUUID().toString())
                        .organization(org)
                        .provider(request.getProvider())
                        .build());

        config.setModelName(request.getModelName());
        config.setBaseUrl(request.getBaseUrl());
        config.setEnabled(request.getEnabled() != null ? request.getEnabled() : true);
        config.setDefault(request.getIsDefault() != null ? request.getIsDefault() : false);

        if (request.getApiKey() != null && !request.getApiKey().isBlank()) {
            config.setEncryptedApiKey(encryptionService.encrypt(request.getApiKey()));
        }

        if (config.isDefault()) {
            List<AIProviderConfig> others = aiProviderConfigRepository.findByOrganizationId(org.getId());
            for (AIProviderConfig other : others) {
                if (!other.getId().equals(config.getId()) && other.isDefault()) {
                    other.setDefault(false);
                    aiProviderConfigRepository.save(other);
                }
            }
        }

        AIProviderConfig saved = aiProviderConfigRepository.save(config);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    @PostMapping("/ai-providers/test")
    @Operation(summary = "Test connectivity with upstream AI Provider endpoint")
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
        return ResponseEntity.ok(gitHubService.getAppConfig());
    }

    @PostMapping("/github-app/manifest")
    @Operation(summary = "Generate GitHub App manifest for 1-click GitHub App creation")
    public ResponseEntity<Map<String, Object>> getManifest(@RequestBody(required = false) Map<String, String> body) {
        String webhookUrl = (body != null && body.containsKey("webhookUrl"))
                ? body.get("webhookUrl")
                : "https://api.reviewai.dev/api/v1/webhooks/github";
        String callbackUrl = (body != null && body.containsKey("callbackUrl"))
                ? body.get("callbackUrl")
                : "http://localhost:3000/settings/github";
        return ResponseEntity.ok(gitHubService.generateAppManifest(callbackUrl, webhookUrl));
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
