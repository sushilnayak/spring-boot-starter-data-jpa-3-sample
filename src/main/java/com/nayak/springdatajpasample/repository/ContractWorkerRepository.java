package com.nayak.springdatajpasample.repository;

import com.nayak.springdatajpasample.entity.contractor.ContractWorker;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface ContractWorkerRepository extends JpaRepository<ContractWorker, Long> {

    @Async // same as @Async("taskExecutor")
    CompletableFuture<List<ContractWorker>> findByAgencyName(String agencyName);

    @Async
    CompletableFuture<ContractWorker> findByContractNumber(String contractNumber);

    @Query("SELECT c FROM ContractWorker c WHERE c.hourlyRate > ?1")
    Stream<ContractWorker> findByHourlyRateGreaterThan(double rate);

    // Return immediately and process asynchronously
    @Async
    CompletableFuture<List<ContractWorker>> findByAgencyNameOrderByHourlyRateDesc(String agencyName);
}