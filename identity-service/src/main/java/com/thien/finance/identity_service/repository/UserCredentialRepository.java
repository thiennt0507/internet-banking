package com.thien.finance.identity_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.identity_service.entity.UserCredential;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long>{
    Optional<UserCredential> findByName(String username);
    UserCredential findByEmail(String email);
}
