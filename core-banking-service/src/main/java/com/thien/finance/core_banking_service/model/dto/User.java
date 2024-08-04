package com.thien.finance.core_banking_service.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String userName;
    private String email;
    private List<BankAccount> bankAccounts;
}