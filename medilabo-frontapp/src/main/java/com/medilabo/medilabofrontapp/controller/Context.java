package com.medilabo.medilabofrontapp.controller;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.medilabo.medilabofrontapp.model.User;

@Component
public class Context {

	private static final Logger log = LoggerFactory.getLogger(Context.class);

	public User loggedUser;

	public String url;
	
	public int patientId;
	
	public Context() {
		this.loggedUser = new User();
		this.url = "/index";
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void resetUrl() {
		this.url = "/index";
	}
	
	public String setAuthHeader() {
		String username = this.loggedUser.getUsername();
		String password = this.loggedUser.getPassword();
		byte[] encodedBytes = Base64.getEncoder().encode((username + ":" + password).getBytes());
		String authHeader = "Basic " + new String(encodedBytes);
		log.info("authHeader in setAuthHeader : {}", authHeader);
		return authHeader;
	}
	
}
