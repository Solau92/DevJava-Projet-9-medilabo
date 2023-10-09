package com.medilabo.medilabogateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
	
	/**
	 * Login method. 
	 * 
	 * @return ResponseEntity<Void> with HTTP status ACCEPTED 
	 */
	@GetMapping("/login")
	public ResponseEntity<Void> login() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

}
