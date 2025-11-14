package com.scalable.billing.service.repository;

import com.scalable.billing.service.entity.BillingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillingRecordRepository extends JpaRepository<BillingRecord, UUID> {
    
    /**
     * Optimized pagination query with index usage
     */
    @Query("SELECT b FROM BillingRecord b WHERE b.customerId = :customerId ORDER BY b.createdAt DESC")
    Page<BillingRecord> findByCustomerIdOrderByCreatedAtDesc(
        @Param("customerId") UUID customerId,
        Pageable pageable
    );
    
    @Query("SELECT b FROM BillingRecord b WHERE b.status = :status")
    List<BillingRecord> findByStatus(@Param("status") String status);
    
    Optional<BillingRecord> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Check if billing exists for period to avoid duplicates
     */
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BillingRecord b " +
           "WHERE b.customerId = :customerId " +
           "AND b.billingPeriodStart = :start AND b.billingPeriodEnd = :end")
    boolean existsByCustomerAndPeriod(
        @Param("customerId") UUID customerId,
        @Param("start") LocalDate start,
        @Param("end") LocalDate end
    );
}
