package com.thien.finance.identity_service.model.rest.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private String name;
    private List<AccountResponse> bankAccounts;
    private Integer id;
    private String email;
}

