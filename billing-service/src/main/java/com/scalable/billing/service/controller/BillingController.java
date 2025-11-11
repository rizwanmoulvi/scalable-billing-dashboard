package com.scalable.billing.service.controller;

import com.scalable.billing.common.dto.BillingRecordDTO;
import com.scalable.billing.common.event.UsageEvent;
import com.scalable.billing.service.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@Slf4j
public class BillingController {
    
    private final BillingService billingService;
    
    /**
     * Ingest usage event
     * POST /api/billing/usage
     */
    @PostMapping("/usage")
    public ResponseEntity<Void> ingestUsage(@Valid @RequestBody UsageEvent event) {
        log.info("Received usage event for customer: {}", event.getCustomerId());
        billingService.ingestUsageEvent(event);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    
    /**
     * Calculate billing for customer and period
     * POST /api/billing/calculate?customerId=xxx&periodStart=2024-01-01&periodEnd=2024-01-31
     */
    @PostMapping("/calculate")
    public ResponseEntity<BillingRecordDTO> calculateBilling(
        @RequestParam UUID customerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd
    ) {
        log.info("Calculating billing for customer {} from {} to {}", customerId, periodStart, periodEnd);
        BillingRecordDTO billing = billingService.calculateBilling(customerId, periodStart, periodEnd);
        return ResponseEntity.ok(billing);
    }
    
    /**
     * Get billing records for customer with pagination
     * GET /api/billing/customer/{customerId}?page=0&size=20
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<BillingRecordDTO>> getBillingRecords(
        @PathVariable UUID customerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        log.debug("Fetching billing records for customer {}, page {}, size {}", customerId, page, size);
        Page<BillingRecordDTO> records = billingService.getBillingRecords(customerId, page, size);
        return ResponseEntity.ok(records);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Billing Service is healthy");
    }
}
