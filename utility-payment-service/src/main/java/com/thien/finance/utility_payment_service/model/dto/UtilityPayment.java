package com.thien.finance.utility_payment_service.model.dto;

import java.math.BigDecimal;

import com.thien.finance.utility_payment_service.model.TransactionStatus;

import lombok.Data;

@Data
public class UtilityPayment extends AuditAware {
    private Long providerId;
    private BigDecimal amount;
    private String referenceNumber;
    private String account;
    private TransactionStatus status;
}
