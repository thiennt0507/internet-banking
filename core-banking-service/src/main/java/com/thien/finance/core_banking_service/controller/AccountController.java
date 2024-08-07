package com.thien.finance.core_banking_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.dto.UtilityAccount;
import com.thien.finance.core_banking_service.model.entity.BankAccountEntity;
import com.thien.finance.core_banking_service.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping(value = "/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/bank-account")
    public ResponseEntity getBankAccount(@RequestParam(name = "account_number") String accountNumber) {
        log.info("Reading account by ID {}", accountNumber);
        return ResponseEntity.ok(accountService.readBankAccount(accountNumber));
    }

    @PostMapping("/bank-account")
    public ResponseEntity<?> createBankAccount(@RequestBody BankAccount bankAccountDto, Authentication authentication) {
        
        return ResponseEntity.ok(accountService.createBankAccount(authentication, bankAccountDto));
    }
    
    @GetMapping("/bank-account/me")
    public ResponseEntity<List<BankAccount>> getBankAccountsMe(@RequestParam(name ="userName") String userName, Authentication authentication) {
            return ResponseEntity.ok(accountService.readBankAccountsMe(authentication, userName));
    }

    @GetMapping("/bank-account/me/{bankAccountNumber}")
    public ResponseEntity<?> getBankAccountMe(@RequestParam(name ="userName") String userName, @PathVariable String bankAccountNumber, Authentication authentication) {
            return ResponseEntity.ok(accountService.readBankAccountMe(authentication, userName, bankAccountNumber));
    }

    @GetMapping(("/util-account") )
    public ResponseEntity<List<?>> getUtilityAccounts() {
        log.info("Reading utility accounts");
        return ResponseEntity.ok(accountService.readUtilityAccounts());
    }

    @GetMapping("/util-account/{account_name}")
    public ResponseEntity getUtilityAccount(@PathVariable("account_name") String providerName) {
        log.info("Reading utitlity account by ID {}", providerName);
        return ResponseEntity.ok(accountService.readUtilityAccount(providerName));
    }

    @PostMapping("/util-account")
    public ResponseEntity createUtilityAccount(@RequestBody UtilityAccount utilityAccount, Authentication authentication) {
        return ResponseEntity.ok(accountService.createUtilityAccount(authentication, utilityAccount));
    }
    
}
