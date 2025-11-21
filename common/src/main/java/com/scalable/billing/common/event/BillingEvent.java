package com.scalable.billing.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Event representing a calculated billing record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingEvent {
    
    @JsonProperty("event_id")
    private String eventId;
    
    @JsonProperty("billing_id")
    private UUID billingId;
    
    @JsonProperty("customer_id")
    private UUID customerId;
    
    @JsonProperty("billing_period_start")
    private LocalDate billingPeriodStart;
    
    @JsonProperty("billing_period_end")
    private LocalDate billingPeriodEnd;
    
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("invoice_number")
    private String invoiceNumber;
    
    @JsonProperty("timestamp")
    private Instant timestamp;
}
