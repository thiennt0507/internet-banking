package com.thien.finance.core_banking_service.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thien.finance.core_banking_service.exception.GlobalErrorCode;
import com.thien.finance.core_banking_service.exception.InsufficientFundsException;
import com.thien.finance.core_banking_service.model.TransactionType;
import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.dto.Transaction;
import com.thien.finance.core_banking_service.model.dto.UtilityAccount;
import com.thien.finance.core_banking_service.model.dto.request.FundTransferRequest;
import com.thien.finance.core_banking_service.model.dto.request.UtilityPaymentRequest;
import com.thien.finance.core_banking_service.model.dto.response.FundTransferResponse;
import com.thien.finance.core_banking_service.model.dto.response.UtilityPaymentResponse;
import com.thien.finance.core_banking_service.model.entity.BankAccountEntity;
import com.thien.finance.core_banking_service.model.entity.TransactionEntity;
import com.thien.finance.core_banking_service.model.entity.UserEntity;
import com.thien.finance.core_banking_service.model.mapper.TransactionMapper;
import com.thien.finance.core_banking_service.repository.BankAccountRepository;
import com.thien.finance.core_banking_service.repository.TransactionRepository;
import com.thien.finance.core_banking_service.repository.UserRepository;
import com.thien.finance.core_banking_service.service.impl.BaseRedisServiceImpl;
import com.thien.finance.event.dto.NotificationEvent;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
// @RequiredArgsConstructor
public class TransactionService extends BaseRedisServiceImpl {
    public TransactionService(RedisTemplate<String, Object> redisTemplate,
                            AccountService accountService,
                            BankAccountRepository bankAccountRepository,
                            TransactionRepository transactionRepository,
                            UserRepository userRepository,
                            KafkaTemplate<String, Object> kafkaTemplate) {
        super(redisTemplate);
        this.accountService = accountService;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;

    }

    private final AccountService accountService;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private TransactionMapper transactinmapper = new TransactionMapper();

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String KEY_PREFIX = "Transaction_";

    // public TransactionService(RedisTemplate<String, Object> redisTemplate,
    //                          AccountService accountService,
    //                          BankAccountRepository bankAccount,
    //                          TransactionRepository transactionRepository,
    //                          KafkaTemplate<String, Object> kafkaTemplate) {
    //     // super(redisTemplate);
    //     this.accountService = accountService;
    //     this.bankAccountRepository = bankAccount;
    //     this.transactionRepository = transactionRepository;
    //     this.kafkaTemplate = kafkaTemplate;
    // }

    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest, Authentication authentication) {
        String currentUserName = authentication.getName();

        Optional<BankAccountEntity> account = checkAccountNumber(currentUserName, fundTransferRequest.getFromAccount());

        if (!account.isPresent()) {
            throw new EntityNotFoundException("This account does not belong to you");
        }

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

        ZonedDateTime zonedDateTimeNow = ZonedDateTime.now(ZoneId.of("UTC"));

        System.out.println("Time now is: " + zonedDateTimeNow.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")));

        String time = zonedDateTimeNow.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));

        TransactionEntity sendTransaction = new TransactionEntity();
        sendTransaction.setTransactionType(TransactionType.UTILITY_PAYMENT);
        sendTransaction.setReferenceNumber(utilityPaymentRequest.getReferenceNumber());
        sendTransaction.setTransactionDate(Timestamp.from(zonedDateTimeNow.toInstant()));
        sendTransaction.setTransactionId(transactionId);
        sendTransaction.setAccount(fromAccount);
        sendTransaction.setAmount(utilityPaymentRequest.getAmount().negate());
        

        transactionRepository.save(sendTransaction);

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

        String time = zonedDateTimeNow.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));

        BankAccountEntity account_sender = bankAccountRepository.save(fromBankAccountEntity);

        NotificationEvent notificationEventSender = NotificationEvent.builder()
                                                .channel("EMAIL")
                                                .recepient(account_sender.getUser().getEmail())
                                                .subject("Thông tin tin chuyển tiền lúc "+  time)
                                                .body("Tài khoản " + fromBankAccount.getNumber() + "của người dùng " + account_sender.getUser().getUserName() +" đã chuyển " + amount +" đến số tài khoản " + toBankAccount.getNumber())
                                                .build();

        kafkaTemplate.send("notification-delivery", notificationEventSender);
        
        TransactionEntity sendTransaction = new TransactionEntity();
        sendTransaction.setTransactionType(TransactionType.FUND_TRANSFER);
        sendTransaction.setReferenceNumber((toBankAccountEntity.getNumber()));
        sendTransaction.setTransactionDate(Timestamp.from(zonedDateTimeNow.toInstant()));
        sendTransaction.setTransactionId(transactionId);
        sendTransaction.setAccount(fromBankAccountEntity);
        sendTransaction.setAmount(amount.negate());

        transactionRepository.save(sendTransaction);

        toBankAccountEntity.setActualBalance(toBankAccountEntity.getActualBalance().add(amount));
        toBankAccountEntity.setAvailableBalance(toBankAccountEntity.getActualBalance().add(amount));
        BankAccountEntity account_reciepent =  bankAccountRepository.save(toBankAccountEntity);

        // Kafka send event
        NotificationEvent notificationEventReciepent = NotificationEvent.builder()
                        .channel("EMAIL")
                        .recepient(account_reciepent.getUser().getEmail())
                        .subject("Thông tin tin nhận tiền lúc "+  time)
                        .body("Tài khoản " + toBankAccount.getNumber() + "của người dùng " + account_reciepent.getUser().getUserName() +" đã nhận " + amount +" từ số tài khoản " + fromBankAccount.getNumber())
                        .build();

        kafkaTemplate.send("notification-delivery", notificationEventReciepent);

        TransactionEntity receivedTransaction = new TransactionEntity();
        receivedTransaction.setTransactionType(TransactionType.FUND_TRANSFER);
        receivedTransaction.setReferenceNumber((toBankAccountEntity.getNumber()));
        receivedTransaction.setTransactionDate(Timestamp.from(zonedDateTimeNow.toInstant()));
        receivedTransaction.setTransactionId(transactionId);
        receivedTransaction.setAccount(toBankAccountEntity);
        receivedTransaction.setAmount(amount);

        transactionRepository.save(receivedTransaction);

        return transactionId;

    }


    public List<Transaction> getAllTransaction(Authentication authentication, String accountNumber) {
        String currentUserName  = authentication.getName();
        Optional<BankAccountEntity> bankAccountEntity = checkAccountNumber(currentUserName, accountNumber);

        if (!bankAccountEntity.isPresent()) {
            throw new EntityNotFoundException("Reference number is not matching with any bank account");
        }

        List<Transaction> transactions = new ArrayList<Transaction>();

        String keyRedis = KEY_PREFIX + currentUserName + "_" + accountNumber;

        Boolean isKeyExist = this.keyExists(keyRedis);

        if (isKeyExist) {
            log.info("Get transaction in redis with username " + currentUserName + " and account number " + accountNumber);

            transactions = (List<Transaction>) this.get(keyRedis);

            this.setTimeToLive(keyRedis, 7L);

            return transactions;
        }

        Optional<List<TransactionEntity>> transactionEntities = transactionRepository.findByAccountId(bankAccountEntity.get().getId());

        transactions = transactinmapper.convertToDtoList(transactionEntities.get());

        log.info("Write transaction to redis");
        
        this.set(keyRedis, transactions);

        return transactions;
    }

    private Optional<BankAccountEntity> checkAccountNumber(String currentUserName, String accountNumber) {
        Optional<UserEntity> currentUserEntity = userRepository.findByUserName(currentUserName);

        
        if (!currentUserEntity.isPresent()) {
            throw new EntityNotFoundException("User not found in database");
        }

        Optional<List<BankAccountEntity>> bankAccountEntities = bankAccountRepository.findByUserId(currentUserEntity.get().getId());

        return bankAccountEntities.get().stream().filter(account -> {

            return account.getNumber().equals(accountNumber);
        }
            
        ).findAny();

    }

}
