package com.medilabo.medilabofrontapp.service;

import com.medilabo.medilabofrontapp.model.User;

public interface AuthenticationService {

	void login(String authHeader, User user);

}
