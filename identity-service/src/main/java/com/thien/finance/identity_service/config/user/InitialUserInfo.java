package com.thien.finance.identity_service.config.user;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.thien.finance.identity_service.model.dto.Role;
import com.thien.finance.identity_service.model.entity.UserCredential;
import com.thien.finance.identity_service.model.repository.UserCredentialRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Initial user data for testing purposes.
 * Replace this class with your own implementation for production environment.
 * 
 * @author Nguyen Thanh THien
 * @since 1.0.0
 * @version 1.0.0
**/

@RequiredArgsConstructor
@Component
@Slf4j
public class InitialUserInfo implements CommandLineRunner{
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        UserCredential manager = new UserCredential();
        manager.setUserName("Manager");
        manager.setPassword(passwordEncoder.encode("password"));
        manager.setRoles(Role.ROLE_MANAGER.name());
        manager.setEmail("manager@manager.com");

        UserCredential admin = new UserCredential();
        admin.setUserName("Admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRoles(Role.ROLE_ADMIN.name());
        admin.setEmail("admin@admin.com");

        UserCredential user = new UserCredential();
        user.setUserName("User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Role.ROLE_USER.name());
        user.setEmail("user@user.com");

        userCredentialRepository.saveAll(List.of(manager,admin,user));
    }
}
