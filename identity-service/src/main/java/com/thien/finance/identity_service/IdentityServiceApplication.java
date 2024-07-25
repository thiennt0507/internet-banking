package com.thien.finance.identity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.thien.finance.identity_service.config.RSAKeyRecord;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(RSAKeyRecord.class)
public class IdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}

}
