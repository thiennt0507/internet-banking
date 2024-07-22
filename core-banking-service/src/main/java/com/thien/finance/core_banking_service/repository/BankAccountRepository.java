package com.thien.finance.core_banking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.core_banking_service.model.entity.BankAccountEntity;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    Optional<BankAccountEntity> findByNumber(String accountNumber);
}
