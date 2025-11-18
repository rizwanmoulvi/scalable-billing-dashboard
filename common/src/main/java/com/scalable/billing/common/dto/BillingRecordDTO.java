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
 * DTO for billing record response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingRecordDTO {
    
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("customer_id")
    private UUID customerId;
    
    @JsonProperty("customer_name")
    private String customerName;
    
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
    
    @JsonProperty("due_date")
    private LocalDate dueDate;
    
    @JsonProperty("paid_date")
    private LocalDate paidDate;
}
