package com.thien.finance.core_banking_service.model.dto;

import java.math.BigDecimal;

import com.thien.finance.core_banking_service.model.AccountStatus;
import com.thien.finance.core_banking_service.model.AccountType;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class BankAccount {
    private Long id;
    private String number;
    private AccountType type;
    private AccountStatus status;
    private BigDecimal availableBalance;
    private BigDecimal actualBalance;
    private User user;
}
