package com.scalable.billing.processor.service;

import com.scalable.billing.common.event.UsageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsageIngestionService {
    
    private final JdbcTemplate jdbcTemplate;
    
    /**
     * Batch insert for high performance (50 records at once)
     */
    @Transactional
    public void processUsageEvents(List<UsageEvent> events) {
        String sql = "INSERT INTO usage_data (customer_id, resource_type, quantity, unit, unit_price, timestamp, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        jdbcTemplate.batchUpdate(sql, events, 50, (ps, event) -> {
            ps.setObject(1, event.getCustomerId());
            ps.setString(2, event.getResourceType());
            ps.setBigDecimal(3, event.getQuantity());
            ps.setString(4, event.getUnit());
            ps.setBigDecimal(5, event.getUnitPrice());
            ps.setTimestamp(6, Timestamp.from(event.getTimestamp()));
        });
        
        log.debug("Batch inserted {} usage events", events.size());
    }
}
