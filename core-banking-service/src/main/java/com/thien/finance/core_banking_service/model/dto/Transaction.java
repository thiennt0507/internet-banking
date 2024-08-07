package com.thien.finance.core_banking_service.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.thien.finance.core_banking_service.model.TransactionType;
import lombok.Data;

@Data
public class Transaction {
    BigDecimal amount;
    TransactionType transactionType;
    String referenceNumber;
    String transactionId;
    Timestamp transactionDate;
    BankAccount account;
}
