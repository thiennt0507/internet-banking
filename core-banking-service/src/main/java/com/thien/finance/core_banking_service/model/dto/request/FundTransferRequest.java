package com.thien.finance.core_banking_service.model.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundTransferRequest {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}

