package com.medilabo.medilabofrontapp.service;

import com.medilabo.medilabofrontapp.model.User;

public interface AuthenticationService {

	/**
	 * Proceeds to login;
	 * 
	 * @param authHeader corresponding to authorization header
	 * @param user corresponding to the username and password filled by the user
	 */
	void login(String authHeader, User user);

}
