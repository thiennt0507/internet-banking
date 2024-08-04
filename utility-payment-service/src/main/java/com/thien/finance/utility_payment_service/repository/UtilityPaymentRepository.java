package com.thien.finance.utility_payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.utility_payment_service.model.entity.UtilityPaymentEntity;

public interface UtilityPaymentRepository extends JpaRepository<UtilityPaymentEntity, Long> {
}
