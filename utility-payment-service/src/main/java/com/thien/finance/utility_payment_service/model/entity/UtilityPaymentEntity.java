package com.thien.finance.utility_payment_service.model.entity;

import java.math.BigDecimal;

import com.thien.finance.utility_payment_service.model.TransactionStatus;
import com.thien.finance.utility_payment_service.model.dto.AuditAware;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "utility_payment")
public class UtilityPaymentEntity extends AuditAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;
    private BigDecimal amount;
    private String referenceNumber;
    private String account;
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
