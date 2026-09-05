package com.jansetu4.portal.analytics.controller;

import com.jansetu4.portal.analytics.dto.ChartDataResponse;
import com.jansetu4.portal.analytics.dto.SummaryResponse;
import com.jansetu4.portal.analytics.service.AnalyticsService;
import com.jansetu4.portal.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<SummaryResponse>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success("Analytics summary fetched successfully", analyticsService.getSummary()));
    }

    @GetMapping("/by-domain")
    public ResponseEntity<ApiResponse<ChartDataResponse>> getByDomain() {
        return ResponseEntity.ok(ApiResponse.success("Domain analytics fetched successfully", analyticsService.getByDomain()));
    }

    @GetMapping("/by-university")
    public ResponseEntity<ApiResponse<ChartDataResponse>> getByUniversity() {
        return ResponseEntity.ok(ApiResponse.success("University analytics fetched successfully", analyticsService.getByUniversity()));
    }

    @GetMapping("/trend")
    public ResponseEntity<ApiResponse<ChartDataResponse>> getTrend(
            @RequestParam(defaultValue = "monthly") String period) {
        return ResponseEntity.ok(ApiResponse.success("Trend analytics fetched successfully", analyticsService.getTrend(period)));
    }
}
