package com.thien.finance.identity_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDto (
        @NotEmpty(message = "User Name must not be empty")
        String name,
        
        @NotEmpty(message = "User email must not be empty") //Neither null nor 0 size
        @Email(message = "Invalid email format")
        String email,

        @NotEmpty(message = "User password must not be empty")
        String password,
        
        @NotNull(message = "role must not be null")
        String role
){}
