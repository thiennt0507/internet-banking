package com.thien.finance.api_gateway.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.thien.finance.api_gateway.dto.ApiResponse;
import com.thien.finance.api_gateway.service.IdentityService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import lombok.AccessLevel;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoints = {
            "/identity/api/v1/bank-users/.*",
            "/identity/users/registration"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO Auto-generated method stub

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);


        // Get token from request header

        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticate(exchange.getResponse());

        String token = authHeader.get(0).replace("Bearer ", "");

        log.info("Token: {}", token);

        return identityService.introspect(token).flatMap(introspectResponse -> {
            if (introspectResponse.getResult().isValid()) {
                return chain.filter(exchange);
            } else {
                return unauthenticate(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unauthenticate(exchange.getResponse()));
    }
    
    private boolean isPublicEndpoint(ServerHttpRequest request){
        return Arrays.stream(publicEndpoints)
        .anyMatch(s -> request.getURI().getPath().matches( s));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticate(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
    
}
