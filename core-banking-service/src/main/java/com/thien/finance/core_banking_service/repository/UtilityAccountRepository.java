package com.thien.finance.core_banking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.core_banking_service.model.entity.UtilityAccountEntity;

import java.util.Optional;

public interface UtilityAccountRepository extends JpaRepository<UtilityAccountEntity, Long> {
    Optional<UtilityAccountEntity> findByProviderName(String provider);
    Optional<UtilityAccountEntity> findByNumber(String number);
}