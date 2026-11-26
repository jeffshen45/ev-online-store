package com.evstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evstore.model.PurchaseRecord;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, Long> {
    List<PurchaseRecord> findByCustomer_IdOrderByPurchasedAtDesc(Long customerId);
}
