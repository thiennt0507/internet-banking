package com.thien.finance.utility_payment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class UtilityPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilityPaymentServiceApplication.class, args);
	}

}
