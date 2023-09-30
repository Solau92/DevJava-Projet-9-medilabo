package com.medilabo.medilaboriskapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.medilabo.medilaboriskapp")
public class MedilaboRiskappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedilaboRiskappApplication.class, args);
	}

}
