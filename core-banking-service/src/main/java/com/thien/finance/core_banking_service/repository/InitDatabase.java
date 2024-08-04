package com.thien.finance.core_banking_service.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.thien.finance.core_banking_service.model.AccountStatus;
import com.thien.finance.core_banking_service.model.AccountType;
import com.thien.finance.core_banking_service.model.Role;
import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.entity.BankAccountEntity;
import com.thien.finance.core_banking_service.model.entity.UserEntity;
import com.thien.finance.core_banking_service.model.entity.UtilityAccountEntity;

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
      UserEntity manager = new UserEntity();
      manager.setUserName("Manager");
      manager.setEmail("manager@manager.com");
      manager.setRole(Role.ROLE_MANAGER.name());

      UserEntity admin = new UserEntity();
      admin.setUserName("Admin");
      admin.setEmail("admin@admin.com");
      admin.setRole(Role.ROLE_ADMIN.name());

      UserEntity user = new UserEntity();
      user.setUserName("User");
      user.setEmail("user@user.com");
      user.setRole(Role.ROLE_USER.name());

      userRepository.saveAll(List.of(manager, admin, user));
      
      
      // Initialize bank accounts
      BankAccountEntity bankAccount1 = new BankAccountEntity();
      bankAccount1.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount1.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount1.setNumber("100015003000");
      bankAccount1.setStatus(AccountStatus.ACTIVE);
      bankAccount1.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount1.setUser(manager);

      BankAccountEntity bankAccount2 = new BankAccountEntity();
      bankAccount2.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount2.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount2.setNumber("100015003001");
      bankAccount2.setStatus(AccountStatus.ACTIVE);
      bankAccount2.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount2.setUser(manager);

      BankAccountEntity bankAccount3 = new BankAccountEntity();
      bankAccount3.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount3.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount3.setNumber("100015003002");
      bankAccount3.setStatus(AccountStatus.ACTIVE);
      bankAccount3.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount3.setUser(admin);

      BankAccountEntity bankAccount4 = new BankAccountEntity();
      bankAccount4.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount4.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount4.setNumber("100015003003");
      bankAccount4.setStatus(AccountStatus.ACTIVE);
      bankAccount4.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount4.setUser(admin);

      BankAccountEntity bankAccount5 = new BankAccountEntity();
      bankAccount5.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount5.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount5.setNumber("100015003004");
      bankAccount5.setStatus(AccountStatus.ACTIVE);
      bankAccount5.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount5.setUser(admin);

      BankAccountEntity bankAccount6 = new BankAccountEntity();
      bankAccount6.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount6.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount6.setNumber("100015003005");
      bankAccount6.setStatus(AccountStatus.ACTIVE);
      bankAccount6.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount6.setUser(admin);

      BankAccountEntity bankAccount7 = new BankAccountEntity();
      bankAccount7.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount7.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount7.setNumber("100015003006");
      bankAccount7.setStatus(AccountStatus.ACTIVE);
      bankAccount7.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount7.setUser(admin);

      BankAccountEntity bankAccount8 = new BankAccountEntity();
      bankAccount8.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount8.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount8.setNumber("100015003007");
      bankAccount8.setStatus(AccountStatus.ACTIVE);
      bankAccount8.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount8.setUser(admin);

      BankAccountEntity bankAccount9 = new BankAccountEntity();
      bankAccount9.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount9.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount9.setNumber("100015003008");
      bankAccount9.setStatus(AccountStatus.ACTIVE);
      bankAccount9.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount9.setUser(user);

      BankAccountEntity bankAccount10 = new BankAccountEntity();
      bankAccount10.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount10.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount10.setNumber("100015003009");
      bankAccount10.setStatus(AccountStatus.ACTIVE);
      bankAccount10.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount10.setUser(user);

      BankAccountEntity bankAccount11 = new BankAccountEntity();
      bankAccount11.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount11.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount11.setNumber("100015003010");
      bankAccount11.setStatus(AccountStatus.ACTIVE);
      bankAccount11.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount11.setUser(user);

      BankAccountEntity bankAccount12 = new BankAccountEntity();
      bankAccount12.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount12.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount12.setNumber("100015003011");
      bankAccount12.setStatus(AccountStatus.ACTIVE);
      bankAccount12.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount12.setUser(user);

      BankAccountEntity bankAccount13 = new BankAccountEntity();
      bankAccount13.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount13.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount13.setNumber("100015003012");
      bankAccount13.setStatus(AccountStatus.ACTIVE);
      bankAccount13.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount13.setUser(user);

      BankAccountEntity bankAccount14 = new BankAccountEntity();
      bankAccount14.setActualBalance(BigDecimal.valueOf(100000.00));
      bankAccount14.setAvailableBalance(BigDecimal.valueOf( 100000.00));
      bankAccount14.setNumber("100015003013");
      bankAccount14.setStatus(AccountStatus.ACTIVE);
      bankAccount14.setType(AccountType.SAVINGS_ACCOUNT);
      bankAccount14.setUser(user);

      bankAccountRepository.saveAll(List.of(bankAccount1, bankAccount2, bankAccount3, bankAccount4, bankAccount5, bankAccount6, bankAccount7, bankAccount8, bankAccount9,
       bankAccount10, bankAccount11, bankAccount12, bankAccount13, bankAccount14));

      // Initialize utility accounts
      UtilityAccountEntity utilityAccount1 = new UtilityAccountEntity();
      utilityAccount1.setNumber("8203232565");
      utilityAccount1.setProviderName("VODAFONE");

      UtilityAccountEntity utilityAccount2 = new UtilityAccountEntity();
      utilityAccount2.setNumber("5464546545");
      utilityAccount2.setProviderName("VERIZON");

      UtilityAccountEntity utilityAccount3 = new UtilityAccountEntity();
      utilityAccount3.setNumber("6546456464");
      utilityAccount3.setProviderName("SINGTEL");

      UtilityAccountEntity utilityAccount4 = new UtilityAccountEntity();
      utilityAccount4.setNumber("7889987999");
      utilityAccount4.setProviderName("HUTCH");

      UtilityAccountEntity utilityAccount5 = new UtilityAccountEntity();
      utilityAccount5.setNumber("2132123132");
      utilityAccount5.setProviderName("AIRTEL");

      UtilityAccountEntity utilityAccount6 = new UtilityAccountEntity();
      utilityAccount6.setNumber("61645564646");
      utilityAccount6.setProviderName("GIO");

      utilityAccountRepository.saveAll(List.of(utilityAccount1, utilityAccount2, utilityAccount3, utilityAccount4, utilityAccount5, utilityAccount6));
    }  
}
