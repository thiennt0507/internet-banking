package com.thien.finance.fun_transfer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FunTransferServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunTransferServiceApplication.class, args);
	}

}
