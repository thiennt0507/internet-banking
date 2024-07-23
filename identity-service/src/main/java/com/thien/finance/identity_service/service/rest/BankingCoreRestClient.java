package com.thien.finance.identity_service.service.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.thien.finance.identity_service.model.rest.response.UserResponse;

@FeignClient(name = "core-banking-service")
public interface BankingCoreRestClient {

    @GetMapping("/api/v1/user/{identification}")
    UserResponse getUser(@PathVariable("identification") String identification);

}
