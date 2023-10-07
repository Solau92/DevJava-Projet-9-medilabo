package com.medilabo.medilabofrontapp.context;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
		this.redirectAfterExceptionUrl = "/index";
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


	public void setRedirectAfterExceptionUrl(String url) {
		this.redirectAfterExceptionUrl = url;
	}
	
	public void resetUrl() {
		this.redirectAfterExceptionUrl = "/index";
	}
	
	public String setAuthHeader() {
		String username = this.loggedUser.getUsername();
		String password = this.loggedUser.getPassword();
		byte[] encodedBytes = Base64.getEncoder().encode((username + ":" + password).getBytes());
		String authHeader = "Basic " + new String(encodedBytes);
		log.info("authHeader in setAuthHeader : {}", authHeader);
		return authHeader;
	}
	
	public void resetPatientId() {
		this.patientId = 0;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
