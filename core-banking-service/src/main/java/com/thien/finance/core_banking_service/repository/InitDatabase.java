package com.thien.finance.core_banking_service.repository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.thien.finance.core_banking_service.model.AccountStatus;
import com.thien.finance.core_banking_service.model.AccountType;
import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.entity.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDatabase implements CommandLineRunner  {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UtilityAccountRepository utilityAccountRepository;

    @Override
    public void run(String... args) throws Exception {
      // Initialize user
      UserEntity user = new UserEntity();
      
      // Initialize bank accounts
      // Initialize transactions
      // Initialize utility accounts
    }  
}
