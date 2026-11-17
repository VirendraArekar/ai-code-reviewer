package com.aicode.reviewer.repository;

import com.aicode.reviewer.entity.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, String> {
    List<OrganizationMember> findByOrganizationId(String organizationId);
    List<OrganizationMember> findByUserId(String userId);
}
