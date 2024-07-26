package com.thien.finance.api_gateway.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.thien.finance.api_gateway.configuration.RSAKeyRecord;

@Service
public class JwtTokenUtil {
    
    @Autowired
    private RSAKeyRecord rsaKeyRecord ;
    
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
    }
}
