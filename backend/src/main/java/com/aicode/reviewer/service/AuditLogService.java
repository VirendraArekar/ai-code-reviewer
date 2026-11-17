package com.aicode.reviewer.service;

import com.aicode.reviewer.entity.AuditLog;
import com.aicode.reviewer.repository.AuditLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditLogService {

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }


    private final AuditLogRepository auditLogRepository;

    @Async
    public void log(String userId, String userEmail, String action, String entityType, String entityId, String ipAddress, String details) {
        AuditLog log = AuditLog.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .userEmail(userEmail)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .ipAddress(ipAddress)
                .details(details)
                .build();
        auditLogRepository.save(log);
    }
}
