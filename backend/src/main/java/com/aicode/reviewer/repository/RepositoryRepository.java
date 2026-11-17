package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryRepository extends JpaRepository<RepositoryEntity, String> {
    List<RepositoryEntity> findByOrganizationId(String organizationId);
    Optional<RepositoryEntity> findByFullName(String fullName);
    Optional<RepositoryEntity> findByGithubRepoId(Long githubRepoId);
    List<RepositoryEntity> findByNameContainingIgnoreCaseOrFullNameContainingIgnoreCase(String name, String fullName);
}
