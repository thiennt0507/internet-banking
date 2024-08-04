package com.thien.finance.identity_service.dto;

import com.thien.finance.identity_service.model.dto.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author Nguyen Thanh Thien
 */
public record UserRegistrationDto (
        @NotEmpty(message = "User Name must not be empty")
        String username,

        @Email(message = "email is not valid")
        @NotEmpty(message = "User email must not be empty") //Neither null nor 0 size
        
        @Email(message = "Invalid email format")
        String email,

        @NotEmpty(message = "User password must not be empty")
        String password,
        @NotNull(message = "role must not be null")
        Role role
){ }
