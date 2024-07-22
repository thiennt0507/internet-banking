package com.thien.finance.core_banking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.core_banking_service.model.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
