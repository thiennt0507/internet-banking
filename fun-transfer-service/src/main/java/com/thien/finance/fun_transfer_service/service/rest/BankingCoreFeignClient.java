package com.thien.finance.fun_transfer_service.service.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thien.finance.fun_transfer_service.configuration.CustomFeignClientConfiguration;
import com.thien.finance.fun_transfer_service.model.dto.request.FundTransferRequest;
import com.thien.finance.fun_transfer_service.model.dto.response.AccountResponse;
import com.thien.finance.fun_transfer_service.model.dto.response.FundTransferResponse;

@FeignClient(value = "core-banking-service", configuration = CustomFeignClientConfiguration.class)
public interface BankingCoreFeignClient {

    @RequestMapping(path = "/api/v1/account/bank-account/{account_number}", method = RequestMethod.GET)
    AccountResponse readAccount(@PathVariable("account_number") String accountNumber);

    @RequestMapping(path = "/api/v1/transaction/fund-transfer", method = RequestMethod.POST)
    FundTransferResponse fundTransfer(@RequestBody FundTransferRequest fundTransferRequest);

}
