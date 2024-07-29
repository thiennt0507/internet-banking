package com.thien.finance.api_gateway.service;

import org.springframework.stereotype.Service;

import com.thien.finance.api_gateway.dto.ApiResponse;
import com.thien.finance.api_gateway.dto.IntrospectResponse;
import com.thien.finance.api_gateway.repository.IdentityClient;
import com.thien.finance.api_gateway.dto.IntrospectRequest;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;
    
    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder()
                        .token(token)
                .build());
    }
}
