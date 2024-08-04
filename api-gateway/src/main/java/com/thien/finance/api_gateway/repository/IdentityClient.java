package com.thien.finance.api_gateway.repository;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.thien.finance.api_gateway.dto.ApiResponse;
import com.thien.finance.api_gateway.dto.ValidateResponse;

import reactor.core.publisher.Mono;

public interface IdentityClient {
    @GetExchange(url = "/api/v1/bank-users/validate")
    Mono<ApiResponse<ValidateResponse>> validate(@RequestParam String token);
}
