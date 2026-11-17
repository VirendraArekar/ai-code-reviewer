package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    List<Team> findByOrganizationId(String organizationId);
}
