package com.teamflow.controller;

import com.teamflow.api.ApiResponse;
import com.teamflow.dto.response.DashboardAnalyticsResponse;
import com.teamflow.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardAnalyticsResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard analytics retrieved", analyticsService.getDashboardAnalytics()));
    }
}

