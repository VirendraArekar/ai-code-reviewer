package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.auth.UserDto;
import com.aicode.reviewer.dto.repo.RepositoryRequest;
import com.aicode.reviewer.dto.repo.RepositoryResponse;
import com.aicode.reviewer.service.AuthService;
import com.aicode.reviewer.service.RepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repositories")
@Tag(name = "Repository Management", description = "Endpoints for managing connected repositories and review configurations")
public class RepositoryController {

    public RepositoryController(RepositoryService repositoryService, AuthService authService) {
        this.repositoryService = repositoryService;
        this.authService = authService;
    }


    private final RepositoryService repositoryService;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "List all repositories for the organization")
    public ResponseEntity<List<RepositoryResponse>> getRepositories(
            @RequestParam(required = false) String search,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(repositoryService.getRepositories(user.getOrganizationId(), search));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get repository details by ID")
    public ResponseEntity<RepositoryResponse> getRepositoryById(@PathVariable String id) {
        return ResponseEntity.ok(repositoryService.getRepositoryById(id));
    }

    @PostMapping
    @Operation(summary = "Connect a new repository")
    public ResponseEntity<RepositoryResponse> createRepository(
            @Valid @RequestBody RepositoryRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(repositoryService.createRepository(request, user.getOrganizationId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update repository review settings and rules")
    public ResponseEntity<RepositoryResponse> updateRepository(
            @PathVariable String id,
            @RequestBody RepositoryRequest request
    ) {
        return ResponseEntity.ok(repositoryService.updateRepository(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Disconnect repository")
    public ResponseEntity<Void> deleteRepository(@PathVariable String id) {
        repositoryService.deleteRepository(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sync")
    @Operation(summary = "Sync repositories from connected GitHub account")
    public ResponseEntity<List<RepositoryResponse>> syncGithubRepositories(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(repositoryService.getRepositories(user.getOrganizationId(), null));
    }
}
