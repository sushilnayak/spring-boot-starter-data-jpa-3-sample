package com.nayak.springdatajpasample.repository;
import com.nayak.springdatajpasample.entity.inheritance.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Async
    CompletableFuture<List<Payment>> findByAmountGreaterThan(BigDecimal amount);

    @Query("SELECT p FROM Payment p WHERE TYPE(p) = :type")
    Page<Payment> findByPaymentType(Class<?> type, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    Page<Payment> findByStatus(String status, Pageable pageable);
}