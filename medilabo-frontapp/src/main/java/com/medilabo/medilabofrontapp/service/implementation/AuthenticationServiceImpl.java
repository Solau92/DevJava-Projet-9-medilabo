package com.medilabo.medilabofrontapp.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.AuthenticationProxy;
import com.medilabo.medilabofrontapp.service.AuthenticationService;

import feign.FeignException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	private final AuthenticationProxy authenticationProxy;
	
	private static Context context;

	public AuthenticationServiceImpl(AuthenticationProxy authenticationProxy, Context context) {
		this.authenticationProxy = authenticationProxy;
		this.context = context;
	}
	
	/**
	 * Proceeds to login.
	 * 
	 * @param authHeader corresponding to authorization header
	 * @param user corresponding to the username and password filled by the user
	 */
	@Override
	public void login(String authHeader, User user) {
		
		try {
			authenticationProxy.login(authHeader);
			context.getLoggedUser().setUsername(user.getUsername());
			context.getLoggedUser().setPassword(user.getPassword());
			
		} catch (FeignException e) {			
			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());
			
			if (e.status() == 401) {
				log.info("Wrong username or password");
				context.setMessage("Wrong username or password");
				context.setRedirectAfterExceptionUrl(HTMLPage.HOME);
			}
		}
		
		String urlTempo = context.getRedirectAfterExceptionUrl();
		context.resetUrl();
		context.setReturnUrl("redirect:" + urlTempo);
	}

	/**
	 * Proceeds to logout. 
	 * 
	 */
	@Override
	public void logout() {
		context.logoutUser();
		log.info("Logged out");
	}

}
