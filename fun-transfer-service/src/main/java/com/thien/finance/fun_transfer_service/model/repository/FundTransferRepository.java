package com.thien.finance.fun_transfer_service.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.fun_transfer_service.model.entity.FundTransferEntity;

public interface FundTransferRepository extends JpaRepository<FundTransferEntity, Long> {
}
