package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.auth.UserDto;
import com.aicode.reviewer.dto.settings.TeamRequest;
import com.aicode.reviewer.dto.settings.TeamResponse;
import com.aicode.reviewer.service.AuthService;
import com.aicode.reviewer.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@Tag(name = "Team Management", description = "Endpoints for engineering squad creation and member management")
public class TeamController {

    public TeamController(TeamService teamService, AuthService authService) {
        this.teamService = teamService;
        this.authService = authService;
    }


    private final TeamService teamService;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "List teams for organization")
    public ResponseEntity<List<TeamResponse>> getTeams(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(teamService.getTeams(user.getOrganizationId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get team details by ID")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable String id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new team")
    public ResponseEntity<TeamResponse> createTeam(
            @Valid @RequestBody TeamRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDto user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(teamService.createTeam(request, user.getOrganizationId()));
    }
}
