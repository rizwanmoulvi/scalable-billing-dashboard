package com.scalable.billing.analytics.controller;

import com.scalable.billing.analytics.service.AnalyticsService;
import com.scalable.billing.common.dto.DailyUsageSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping("/usage/daily")
    public ResponseEntity<List<DailyUsageSummary>> getDailyUsage(
        @RequestParam UUID customerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<DailyUsageSummary> summary = analyticsService.getDailyUsageSummary(customerId, startDate, endDate);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/cost/trend")
    public ResponseEntity<Map<String, Object>> getCostTrend(@RequestParam(defaultValue = "30") int days) {
        Map<String, Object> trend = analyticsService.getCostTrend(days);
        return ResponseEntity.ok(trend);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Analytics Service is healthy");
    }
}
