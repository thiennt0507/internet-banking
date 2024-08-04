package com.thien.finance.core_banking_service.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thien.finance.core_banking_service.exception.GlobalErrorCode;
import com.thien.finance.core_banking_service.exception.InsufficientFundsException;
import com.thien.finance.core_banking_service.model.TransactionType;
import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.dto.UtilityAccount;
import com.thien.finance.core_banking_service.model.dto.request.FundTransferRequest;
import com.thien.finance.core_banking_service.model.dto.request.UtilityPaymentRequest;
import com.thien.finance.core_banking_service.model.dto.response.FundTransferResponse;
import com.thien.finance.core_banking_service.model.dto.response.UtilityPaymentResponse;
import com.thien.finance.core_banking_service.model.entity.BankAccountEntity;
import com.thien.finance.core_banking_service.model.entity.TransactionEntity;
import com.thien.finance.core_banking_service.repository.BankAccountRepository;
import com.thien.finance.core_banking_service.repository.TransactionRepository;
import com.thien.finance.core_banking_service.service.impl.BaseRedisServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionService extends BaseRedisServiceImpl{
    private final AccountService accountService;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public static final String HASH_KEY = "User";

    public TransactionService(RedisTemplate<String, Object> redisTemplate, AccountService accountService, BankAccountRepository bankAccount, TransactionRepository transactionRepository) {
        super(redisTemplate);
        this.accountService = accountService;
        this.bankAccountRepository = bankAccount;
        this.transactionRepository = transactionRepository;
    }

    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest, Authentication authentication) {
        String currentUser = authentication.getName();
        BankAccount fromBankAccount = accountService.readBankAccount(fundTransferRequest.getFromAccount());

        // if (!currentUser.equals(fromBankAccount.getUser().getUserName())) {
        //     throw new IllegalArgumentException("You are not authorized to perform this operation");
        // }

        
        BankAccount toBankAccount = accountService.readBankAccount(fundTransferRequest.getToAccount());

        //validating account balances
        validateBalance(fromBankAccount, fundTransferRequest.getAmount());

        String transactionId = internalFundTransfer(fromBankAccount, toBankAccount, fundTransferRequest.getAmount());
        return FundTransferResponse.builder().message("Transaction successfully completed").transactionId(transactionId).build();
    }

    public UtilityPaymentResponse utilPayment(UtilityPaymentRequest utilityPaymentRequest) {

        String transactionId = UUID.randomUUID().toString();

        BankAccount fromBankAccount = accountService.readBankAccount(utilityPaymentRequest.getAccount());

        //validating account balances
        validateBalance(fromBankAccount, utilityPaymentRequest.getAmount());

        UtilityAccount utilityAccount = accountService.readUtilityAccount(utilityPaymentRequest.getProviderId());

        BankAccountEntity fromAccount = bankAccountRepository.findByNumber(fromBankAccount.getNumber()).get();

        //we can call third party API to process UTIL payment from payment provider from here.

        fromAccount.setActualBalance(fromAccount.getActualBalance().subtract(utilityPaymentRequest.getAmount()));
        fromAccount.setAvailableBalance(fromAccount.getActualBalance().subtract(utilityPaymentRequest.getAmount()));

        transactionRepository.save(TransactionEntity.builder().transactionType(TransactionType.UTILITY_PAYMENT)
            .account(fromAccount)
            .transactionId(transactionId)
            .referenceNumber(utilityPaymentRequest.getReferenceNumber())
            .amount(utilityPaymentRequest.getAmount().negate()).build());

        return UtilityPaymentResponse.builder().message("Utility payment successfully completed")
            .transactionId(transactionId).build();
    }

    private void validateBalance(BankAccount bankAccount, BigDecimal amount) {
        if (bankAccount.getActualBalance().compareTo(BigDecimal.ZERO) < 0 || bankAccount.getActualBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the account " + bankAccount.getNumber(), GlobalErrorCode.INSUFFICIENT_FUNDS);
        }
    }

    public String internalFundTransfer(BankAccount fromBankAccount, BankAccount toBankAccount, BigDecimal amount) {

        String transactionId = UUID.randomUUID().toString();

        BankAccountEntity fromBankAccountEntity = bankAccountRepository.findByNumber(fromBankAccount.getNumber()).orElseThrow(EntityNotFoundException::new);
        BankAccountEntity toBankAccountEntity = bankAccountRepository.findByNumber(toBankAccount.getNumber()).orElseThrow(EntityNotFoundException::new);

        fromBankAccountEntity.setActualBalance(fromBankAccountEntity.getActualBalance().subtract(amount));
        fromBankAccountEntity.setAvailableBalance(fromBankAccountEntity.getActualBalance().subtract(amount));

        ZonedDateTime zonedDateTimeNow = ZonedDateTime.now(ZoneId.of("UTC"));

        System.out.println("Time now is: " + zonedDateTimeNow.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")));

        bankAccountRepository.save(fromBankAccountEntity);

        transactionRepository.save(TransactionEntity.builder()
            .transactionType(TransactionType.FUND_TRANSFER)
            .referenceNumber(toBankAccountEntity.getNumber())
            .transactionDate(Timestamp.from(zonedDateTimeNow.toInstant()))
            .transactionId(transactionId)
            .account(fromBankAccountEntity).amount(amount.negate())
            .build());

        toBankAccountEntity.setActualBalance(toBankAccountEntity.getActualBalance().add(amount));
        toBankAccountEntity.setAvailableBalance(toBankAccountEntity.getActualBalance().add(amount));
        bankAccountRepository.save(toBankAccountEntity);

        transactionRepository.save(TransactionEntity.builder()
            .transactionType(TransactionType.FUND_TRANSFER)
            .referenceNumber(toBankAccountEntity.getNumber())
            .transactionId(transactionId)
            .transactionDate(Timestamp.from(zonedDateTimeNow.toInstant()))
            .account(toBankAccountEntity)
            .amount(amount)
            .build());

        return transactionId;

    }

}
