package com.scalable.billing.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for daily usage summary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyUsageSummary {
    
    @JsonProperty("customer_id")
    private UUID customerId;
    
    @JsonProperty("usage_date")
    private LocalDate usageDate;
    
    @JsonProperty("resource_type")
    private String resourceType;
    
    @JsonProperty("total_quantity")
    private BigDecimal totalQuantity;
    
    @JsonProperty("total_cost")
    private BigDecimal totalCost;
    
    @JsonProperty("event_count")
    private Long eventCount;
}
