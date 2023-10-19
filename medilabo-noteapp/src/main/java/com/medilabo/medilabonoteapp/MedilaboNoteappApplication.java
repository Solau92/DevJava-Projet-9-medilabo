package com.medilabo.medilabonoteapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedilaboNoteappApplication {

	public static void main(String[] args) {
		
		System.setProperty("spring.profiles.active","prod");

		SpringApplication.run(MedilaboNoteappApplication.class, args);
	}

}
