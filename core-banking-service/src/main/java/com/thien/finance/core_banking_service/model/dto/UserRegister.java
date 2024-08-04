package com.thien.finance.core_banking_service.model.dto;


import com.thien.finance.core_banking_service.model.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserRegister {
    private String userName;
    private String email;
    private Role role;
}
