package com.medilabo.medilabofrontapp.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.ArgumentMatchers.any;

import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.AuthenticationProxy;
import com.medilabo.medilabofrontapp.service.implementation.AuthenticationServiceImpl;
import com.medilabo.medilabofrontapp.service.implementation.PatientServiceImpl;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
	
	@InjectMocks
	AuthenticationServiceImpl authenticationService;
	
	@Mock
	AuthenticationProxy authenticationProxy;
	
	@Mock
	Context context;
	
	LogCaptor logCaptor = LogCaptor.forClass(AuthenticationServiceImpl.class);

	User loggedUser;
	String header;
	FeignException unauthorizedException;

	@BeforeEach
	void setUp() {

		loggedUser = new User();
		loggedUser.setUsername("username");
		loggedUser.setPassword("password");

		header = "ok";
		
		unauthorizedException = new FeignException.Unauthorized("", Request.create(HttpMethod.GET, "", new HashMap(), new byte[0], Charset.defaultCharset()), new byte[0], new HashMap<>());
	}
	
	@Test
	void login_Ok_Test() {
		
		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);
		
		// WHEN 
		authenticationService.login(header, loggedUser);
		
		// THEN 
		assertEquals("username", loggedUser.getUsername());
	}
	
	@Test
	void login_Forbidden_Test() {
		
		// GIVEN
		doThrow(unauthorizedException).when(authenticationProxy).login(any(String.class));
		
		// WHEN 
		authenticationService.login(header, loggedUser);
	
		
		// THEN 
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Wrong username or password"));
	}
	
	@Test
	void logout_Ok_Test() {
		
		// GIVEN
		
		// WHEN 
		authenticationService.logout();
		
		// THEN 
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("Logged out"));
	}

}
