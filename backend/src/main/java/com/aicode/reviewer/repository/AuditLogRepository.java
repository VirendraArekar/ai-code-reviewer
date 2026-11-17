package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    List<AuditLog> findAllByOrderByCreatedAtDesc();
    List<AuditLog> findByActionContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrderByCreatedAtDesc(String action, String userEmail);
}
