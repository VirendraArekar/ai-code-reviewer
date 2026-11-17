package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.AIProviderConfig;
import com.aicode.reviewer.entity.enums.AIProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AIProviderConfigRepository extends JpaRepository<AIProviderConfig, String> {
    List<AIProviderConfig> findByOrganizationId(String organizationId);
    Optional<AIProviderConfig> findByOrganizationIdAndProvider(String organizationId, AIProvider provider);
    Optional<AIProviderConfig> findByOrganizationIdAndIsDefaultTrue(String organizationId);
}
