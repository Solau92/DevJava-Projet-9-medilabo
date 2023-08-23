package com.medilabo.medilabofrontapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.medilabo.medilabofrontapp")
public class MedilaboFrontappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedilaboFrontappApplication.class, args);
	}

}
