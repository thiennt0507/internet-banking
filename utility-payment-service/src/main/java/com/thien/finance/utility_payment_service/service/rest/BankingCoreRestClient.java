package com.thien.finance.utility_payment_service.service.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thien.finance.utility_payment_service.configuration.CustomFeignClientConfiguration;
import com.thien.finance.utility_payment_service.model.rest.request.UtilityPaymentRequest;
import com.thien.finance.utility_payment_service.model.rest.response.AccountResponse;
import com.thien.finance.utility_payment_service.model.rest.response.UtilityPaymentResponse;

@FeignClient(name = "CORE-BANKING-SERVICE", configuration = CustomFeignClientConfiguration.class)
public interface BankingCoreRestClient {

    @RequestMapping(path = "/api/v1/account/bank-account/{account_number}", method = RequestMethod.GET)
    AccountResponse readAccount(@PathVariable("account_number") String accountNumber);

    @RequestMapping(path = "/api/v1/transaction/util-payment", method = RequestMethod.POST)
    UtilityPaymentResponse utilityPayment(@RequestBody UtilityPaymentRequest paymentRequest);

}
