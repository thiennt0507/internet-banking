package com.thien.finance.api_gateway.configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record RSAKeyRecord (RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {}
