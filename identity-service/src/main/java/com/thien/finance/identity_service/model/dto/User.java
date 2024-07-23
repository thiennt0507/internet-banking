package com.thien.finance.identity_service.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AuditAware {
    private Long id;

    private String email;

    private String identification;

    private String password;

    private String authId;

    private Status status;

    private Boolean verifyEmail;

    private Boolean enableUser;

    private Status role;
    
}
