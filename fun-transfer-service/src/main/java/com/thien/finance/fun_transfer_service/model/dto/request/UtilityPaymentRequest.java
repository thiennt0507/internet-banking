package com.thien.finance.fun_transfer_service.model.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UtilityPaymentRequest {
    private Long providerId;
    private BigDecimal amount;
    private String referenceNumber;
    private String account;
}
