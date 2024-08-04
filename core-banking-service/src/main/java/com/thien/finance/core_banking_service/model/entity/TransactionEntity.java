package com.thien.finance.core_banking_service.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.thien.finance.core_banking_service.model.TransactionType;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "banking_core_transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private String referenceNumber;

    private String transactionId;

    private Timestamp transactionDate;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private BankAccountEntity account;
}
