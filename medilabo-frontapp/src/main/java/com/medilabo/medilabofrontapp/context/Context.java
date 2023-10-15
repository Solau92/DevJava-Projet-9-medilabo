package com.medilabo.medilabofrontapp.context;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.model.User;

@Component
public class Context {

	private static final Logger log = LoggerFactory.getLogger(Context.class);

	private User loggedUser;

	private String redirectAfterExceptionUrl;
	
	private String returnUrl;
	
	private String message;
	
	private int patientId;
	
	public Context() {
		this.loggedUser = new User();
		this.redirectAfterExceptionUrl = HTMLPage.INDEX;
	}
	
	/**
	 * @return the loggedUser
	 */
	public User getLoggedUser() {
		return loggedUser;
	}

	/**
	 * @param loggedUser the loggedUser to set
	 */
	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	/**
	 * @return the patientId
	 */
	public int getPatientId() {
		return patientId;
	}

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return the url
	 */
	public String getRedirectAfterExceptionUrl() {
		return redirectAfterExceptionUrl;
	}

	/**
	 * @param url the url to set
	 */
	public void setRedirectAfterExceptionUrl(String url) {
		this.redirectAfterExceptionUrl = url;
	}
	
	/**
	 * @return the url
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * @param url the url to set
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	/**
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Sets the redirectAfterExceptionUrl to index page 
	 */
	public void resetUrl() {
		this.redirectAfterExceptionUrl = HTMLPage.INDEX;
	}
	
	/**
	 * Sets the authorization header with username and password.
	 * 
	 * @return the authorization header
	 */
	public String setAuthHeader() {
		String username = this.loggedUser.getUsername();
		String password = this.loggedUser.getPassword();
		byte[] encodedBytes = Base64.getEncoder().encode((username + ":" + password).getBytes());
		String authHeader = "Basic " + new String(encodedBytes);
		log.info("authHeader in setAuthHeader : {}", authHeader);
		return authHeader;
	}
	
	/**
	 * Sets patient id to 0.
	 */
	public void resetPatientId() {
		this.patientId = 0;
	}
	
	/*
	 * Reset the Logged User
	 */
	public void logoutUser() {
		this.loggedUser = new User();
	}
	
}
