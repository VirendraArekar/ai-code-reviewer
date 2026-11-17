package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.auth.UserDto;
import com.aicode.reviewer.dto.review.ReviewFindingResponse;
import com.aicode.reviewer.dto.review.UpdateFindingStatusRequest;
import com.aicode.reviewer.entity.enums.FindingCategory;
import com.aicode.reviewer.entity.enums.FindingSeverity;
import com.aicode.reviewer.entity.enums.FindingStatus;
import com.aicode.reviewer.service.AuthService;
import com.aicode.reviewer.service.FindingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/findings")
@Tag(name = "Findings Management", description = "Endpoints for filtering, updating status, and assigning code findings")
public class FindingController {

    public FindingController(FindingService findingService, AuthService authService) {
        this.findingService = findingService;
        this.authService = authService;
    }


    private final FindingService findingService;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "List findings with severity, category, status, and text filters")
    public ResponseEntity<List<ReviewFindingResponse>> getFindings(
            @RequestParam(required = false) FindingSeverity severity,
            @RequestParam(required = false) FindingCategory category,
            @RequestParam(required = false) FindingStatus status,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(findingService.getFindings(severity, category, status, search));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get finding by ID")
    public ResponseEntity<ReviewFindingResponse> getFindingById(@PathVariable String id) {
        return ResponseEntity.ok(findingService.getFindingById(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update finding status (RESOLVED, IGNORED, FALSE_POSITIVE, OPEN)")
    public ResponseEntity<ReviewFindingResponse> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateFindingStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(findingService.updateStatus(id, request, user.getId()));
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign finding to developer")
    public ResponseEntity<ReviewFindingResponse> assignFinding(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        String userId = body.get("userId");
        return ResponseEntity.ok(findingService.assignFinding(id, userId));
    }
}
