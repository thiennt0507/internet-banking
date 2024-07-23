package com.thien.finance.identity_service.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thien.finance.identity_service.model.entity.UserCredential;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long>{
    Optional<UserCredential> findByName(String username);
    List<UserCredential> findByEmail(String email);
}
