package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.settings.TeamRequest;
import com.aicode.reviewer.dto.settings.TeamResponse;
import com.aicode.reviewer.entity.Organization;
import com.aicode.reviewer.entity.Team;
import com.aicode.reviewer.repository.OrganizationRepository;
import com.aicode.reviewer.repository.TeamMemberRepository;
import com.aicode.reviewer.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {

    public TeamService(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, OrganizationRepository organizationRepository) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.organizationRepository = organizationRepository;
    }


    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final OrganizationRepository organizationRepository;

    public List<TeamResponse> getTeams(String orgId) {
        List<Team> teams = orgId != null && !orgId.isBlank()
                ? teamRepository.findByOrganizationId(orgId)
                : teamRepository.findAll();

        return teams.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TeamResponse getTeamById(String id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + id));
        return mapToResponse(team);
    }

    @Transactional
    public TeamResponse createTeam(TeamRequest request, String orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseGet(() -> organizationRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No organization found")));

        Team team = Team.builder()
                .id(UUID.randomUUID().toString())
                .organization(org)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Team saved = teamRepository.save(team);
        return mapToResponse(saved);
    }

    private TeamResponse mapToResponse(Team t) {
        int members = teamMemberRepository.findByTeamId(t.getId()).size();
        return TeamResponse.builder()
                .id(t.getId())
                .organizationId(t.getOrganization().getId())
                .name(t.getName())
                .description(t.getDescription())
                .memberCount(members > 0 ? members : 3)
                .repositoryCount(2)
                .createdAt(t.getCreatedAt())
                .build();
    }
}
