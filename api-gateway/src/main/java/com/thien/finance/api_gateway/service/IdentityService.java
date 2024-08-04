package com.thien.finance.api_gateway.service;

import org.springframework.stereotype.Service;

import com.thien.finance.api_gateway.dto.ApiResponse;
import com.thien.finance.api_gateway.dto.ValidateResponse;
import com.thien.finance.api_gateway.repository.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<ValidateResponse>> validate(String token) {
        return identityClient.validate(token);
    }
}
