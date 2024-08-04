package com.thien.finance.identity_service.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api-message")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    
    // @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN','ROLE_USER')")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_MANAGER')")
    @GetMapping("/welcome-message")
    public ResponseEntity<String> getFirstWelcomeMessage(Authentication authentication){

        var authenticate = SecurityContextHolder.getContext().getAuthentication();
        log.info("Roles: {}", authenticate.getAuthorities());
        return ResponseEntity.ok("Welcome to the JWT Tutorial: " + authentication.getName()+" with scope:"+ authenticate.getAuthorities());
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> x(){
        return ResponseEntity.ok("Welcome to the JWT Tutorial:");
    }

    // @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/manager-message")
    public ResponseEntity<String> getManagerData(Principal principal){
        return ResponseEntity.ok("Manager::" + principal.getName());
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/admin-message")
    public ResponseEntity<String> getAdminData(@RequestParam("message") String message, Principal principal){
        return ResponseEntity.ok("Admin::"+principal.getName() + " has this message:"+ message);
    }
}
