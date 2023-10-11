package com.medilabo.medilabogateway.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medilabo.medilabogateway.controller.AuthController;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
	
	@InjectMocks
	AuthController authController;
	
	@Test
	void login_Ok_Test() {
		
		// GIVEN 	
		// WHEN 
		ResponseEntity<Void> result = authController.login();
		
		// THEN 
		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
	}

}
