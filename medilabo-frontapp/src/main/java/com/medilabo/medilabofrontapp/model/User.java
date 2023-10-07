package com.medilabo.medilabofrontapp.model;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;

@Component
public class User {

	@NotBlank(message = "Username cannot be empty")
	private String username;
	
	@NotBlank(message = "Password cannot be empty")
	private String password;
	
	public User() {
	
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + "]";
	}
	
}
