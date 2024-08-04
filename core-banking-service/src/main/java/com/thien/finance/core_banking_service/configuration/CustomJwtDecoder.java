package com.thien.finance.core_banking_service.configuration;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder{
    private final RSAKeyRecord rsaKeyRecord;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

        Jwt jwtToken = jwtDecoder.decode(token);
        log.info("Configuration-customeJwtDecoder: " + jwtToken);


        return jwtToken;
        } catch (Exception e) {
            log.error(token, e);
            throw new JwtException("Invalid JWT token", e);
        }
    }
    
}
