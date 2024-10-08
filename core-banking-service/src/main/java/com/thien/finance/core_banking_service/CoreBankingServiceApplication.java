package com.thien.finance.core_banking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.thien.finance.core_banking_service.configuration.RSAKeyRecord;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
// @ComponentScan("application")
public class CoreBankingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreBankingServiceApplication.class, args);
	}

	// @Bean
	// public RedisTemplate<String, JSONArray> redisTemplate() {
	// 	return new RedisTemplate<String, JSONArray>();
	// }

}
