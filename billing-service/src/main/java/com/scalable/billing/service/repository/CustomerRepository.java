package com.scalable.billing.service.repository;

import com.scalable.billing.service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    
    Optional<Customer> findByEmail(String email);
    
    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE'")
    List<Customer> findAllActive();
    
    @Query("SELECT c FROM Customer c WHERE c.planType = :planType AND c.status = 'ACTIVE'")
    List<Customer> findByPlanTypeAndActive(String planType);
}
