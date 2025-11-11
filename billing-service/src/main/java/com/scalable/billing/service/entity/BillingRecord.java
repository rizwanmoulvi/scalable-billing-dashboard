package com.scalable.billing.service.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "billing_records", indexes = {
    @Index(name = "idx_billing_customer", columnList = "customer_id"),
    @Index(name = "idx_billing_period", columnList = "billing_period_start,billing_period_end"),
    @Index(name = "idx_billing_status", columnList = "status"),
    @Index(name = "idx_billing_invoice", columnList = "invoice_number")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;
    
    @Column(name = "billing_period_start", nullable = false)
    private LocalDate billingPeriodStart;
    
    @Column(name = "billing_period_end", nullable = false)
    private LocalDate billingPeriodEnd;
    
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(nullable = false, length = 50)
    private String status = "PENDING";
    
    @Column(name = "invoice_number", unique = true, length = 100)
    private String invoiceNumber;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "paid_date")
    private LocalDate paidDate;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
