package com.thien.finance.core_banking_service.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thien.finance.core_banking_service.model.dto.UserRegister;
import com.thien.finance.core_banking_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<?> readMe(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.readMe(authentication));
        } catch (AuthorizationDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> readUsers(Authentication authentication, Pageable pageable) {
        try {
            return ResponseEntity.ok(userService.readUsers(pageable));
        } catch (AuthorizationDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> readUser(@RequestParam String username, Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.readUser(username, authentication));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public String createUser(@RequestBody UserRegister userRegister) {
        try {
            userService.createUser(userRegister);
            return "Create a new user";
        } catch (Exception e) {
           return "Error creating user";
        }
    }

    @PostMapping("/update")
    public String updateUser(@RequestBody UserRegister userRegister) {
        try {
            userService.updateUser(userRegister);
            return "Update user";
        } catch (Exception e) {
            return "Error updating user";
        }
    }
    
}
