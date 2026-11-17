package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.settings.AuditLogResponse;
import com.aicode.reviewer.repository.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
@Tag(name = "System Administration", description = "Endpoints reserved for Super Admins and Platform Administrators")
public class AdminController {

    public AdminController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }


    private final AuditLogRepository auditLogRepository;

    @GetMapping("/audit-logs")
    @Operation(summary = "Query system audit logs with optional search filter")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogs(@RequestParam(required = false) String search) {
        var list = search != null && !search.isBlank()
                ? auditLogRepository.findByActionContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrderByCreatedAtDesc(search, search)
                : auditLogRepository.findAllByOrderByCreatedAtDesc();

        List<AuditLogResponse> responses = list.stream().map(log -> AuditLogResponse.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .userEmail(log.getUserEmail())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .ipAddress(log.getIpAddress())
                .details(log.getDetails())
                .createdAt(log.getCreatedAt())
                .build()).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}
