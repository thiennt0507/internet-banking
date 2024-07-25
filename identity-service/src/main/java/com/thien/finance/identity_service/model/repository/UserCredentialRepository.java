package com.thien.finance.identity_service.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.identity_service.model.entity.UserCredential;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long>{
    Optional<UserCredential> findByUserName(String username);
    Optional<UserCredential> findByEmail(String email);
}
