package com.medilabo.medilabofrontapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.controller.ClientController;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.AuthenticationProxy;

import feign.FeignException;

@Service
public class AuthenticationService {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

	private final AuthenticationProxy authenticationProxy;
	
	private static Context context;

	public AuthenticationService(AuthenticationProxy authenticationProxy, Context context) {
		this.authenticationProxy = authenticationProxy;
		this.context = context;
	}
	
	public String login(String authHeader, User user) {
		
		try {
			authenticationProxy.login(authHeader);
			context.getLoggedUser().setUsername(user.getUsername());
			context.getLoggedUser().setPassword(user.getPassword());
			
		} catch (FeignException e) {
			
			if (e.status() == 401) {
				log.info("Exception status login in authenticationService: {}", e.status());
				context.setUrl("/");
			}
		}
		
		String urlTempo = context.getUrl();
		context.resetUrl();
		
		return "redirect:" + urlTempo;
	}
	
	

}
