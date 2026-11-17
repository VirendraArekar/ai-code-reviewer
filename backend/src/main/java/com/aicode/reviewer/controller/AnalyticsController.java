package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.analytics.DashboardStatsResponse;
import com.aicode.reviewer.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics & Insights", description = "Endpoints for engineering analytics, velocity charts, and quality metrics")
public class AnalyticsController {

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }


    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get database-driven dashboard statistics and charts data")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }
}
