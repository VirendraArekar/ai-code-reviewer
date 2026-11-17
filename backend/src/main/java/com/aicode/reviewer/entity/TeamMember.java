package com.aicode.reviewer.entity;

import com.aicode.reviewer.entity.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_members")
public class TeamMember {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role = Role.DEVELOPER;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public TeamMember() {}

    public TeamMember(String id, Team team, User user, Role role, LocalDateTime joinedAt) {
        this.id = id;
        this.team = team;
        this.user = user;
        this.role = role != null ? role : Role.DEVELOPER;
        this.joinedAt = joinedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private Team team;
        private User user;
        private Role role = Role.DEVELOPER;
        private LocalDateTime joinedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder team(Team team) { this.team = team; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder joinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; return this; }
        public TeamMember build() { return new TeamMember(id, team, user, role, joinedAt); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
}
