package com.aicode.reviewer.entity;

import com.aicode.reviewer.entity.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization_members")
public class OrganizationMember {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role = Role.DEVELOPER;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public OrganizationMember() {}

    public OrganizationMember(String id, Organization organization, User user, Role role, LocalDateTime joinedAt) {
        this.id = id;
        this.organization = organization;
        this.user = user;
        this.role = role != null ? role : Role.DEVELOPER;
        this.joinedAt = joinedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private Organization organization;
        private User user;
        private Role role = Role.DEVELOPER;
        private LocalDateTime joinedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder organization(Organization organization) { this.organization = organization; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder joinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; return this; }
        public OrganizationMember build() { return new OrganizationMember(id, organization, user, role, joinedAt); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
}
