package com.scalable.billing.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Event representing a usage data point from a customer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageEvent {
    
    @JsonProperty("event_id")
    private String eventId;
    
    @JsonProperty("customer_id")
    private UUID customerId;
    
    @JsonProperty("resource_type")
    private String resourceType;
    
    @JsonProperty("quantity")
    private BigDecimal quantity;
    
    @JsonProperty("unit")
    private String unit;
    
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;
    
    @JsonProperty("timestamp")
    private Instant timestamp;
    
    @JsonProperty("metadata")
    private Map<String, Object> metadata;
    
    public static UsageEvent createDefault(UUID customerId, String resourceType, BigDecimal quantity) {
        return UsageEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .customerId(customerId)
                .resourceType(resourceType)
                .quantity(quantity)
                .unit("unit")
                .unitPrice(BigDecimal.ZERO)
                .timestamp(Instant.now())
                .build();
    }
}
