package com.thien.finance.fun_transfer_service.model.entity;

import java.math.BigDecimal;

import com.thien.finance.fun_transfer_service.model.TransactionStatus;
import com.thien.finance.fun_transfer_service.model.dto.AuditAware;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fund_transfer")
public class FundTransferEntity extends AuditAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionReference;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
