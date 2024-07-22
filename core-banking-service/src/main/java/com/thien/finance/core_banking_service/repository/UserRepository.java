package com.thien.finance.core_banking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.core_banking_service.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByIdentificationNumber(String identificationNumber);
}
