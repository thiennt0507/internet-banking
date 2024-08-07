package com.thien.finance.core_banking_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thien.finance.core_banking_service.model.dto.request.FundTransferRequest;
import com.thien.finance.core_banking_service.model.dto.request.UtilityPaymentRequest;
import com.thien.finance.core_banking_service.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/fund-transfer")
    public ResponseEntity<?> fundTransfer(@RequestBody FundTransferRequest fundTransferRequest, Authentication authentication) {

        log.info("Fund transfer initiated in core bank from {}", fundTransferRequest.toString());
        try {
            
            return ResponseEntity.ok(transactionService.fundTransfer(fundTransferRequest, authentication));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getTransactionFromAccount(Authentication authentication, @RequestParam(value = "accountNumber") String accountNumber) {
        log.info("Get transaction from account " + accountNumber );
        try {
            return ResponseEntity.ok(transactionService.getAllTransaction(authentication, accountNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    

    @PostMapping("/util-payment")
    public ResponseEntity<?> utilPayment(@RequestBody UtilityPaymentRequest utilityPaymentRequest) {

        log.info("Utility Payment initiated in core bank from {}", utilityPaymentRequest.toString());
        return ResponseEntity.ok(transactionService.utilPayment(utilityPaymentRequest));
    }

}
