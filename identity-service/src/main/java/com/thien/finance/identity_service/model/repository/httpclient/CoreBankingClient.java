package com.thien.finance.identity_service.model.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.thien.finance.identity_service.model.dto.UserCreationRequest;

@FeignClient(name = "CORE-BANKING-SERVICE")
public interface CoreBankingClient {
    @PostMapping(value = "/api/v1/user/create", produces = MediaType.APPLICATION_JSON_VALUE)
    String createUser(@RequestBody UserCreationRequest userCreationRequest); 

    @PostMapping(value = "/api/v1/user/update", produces = MediaType.APPLICATION_JSON_VALUE)
    String updateUser(@RequestBody UserCreationRequest userCreationRequest); 
}
