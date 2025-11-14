package com.scalable.billing.service.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "usage_data", indexes = {
    @Index(name = "idx_usage_customer_time", columnList = "customer_id,timestamp"),
    @Index(name = "idx_usage_resource_time", columnList = "resource_type,timestamp"),
    @Index(name = "idx_usage_timestamp", columnList = "timestamp")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private java.util.UUID customerId;
    
    @Column(name = "resource_type", nullable = false, length = 100)
    private String resourceType;
    
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantity;
    
    @Column(nullable = false, length = 50)
    private String unit;
    
    @Column(name = "unit_price", precision = 10, scale = 4)
    private BigDecimal unitPrice;
    
    @Column(nullable = false)
    private Instant timestamp;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
