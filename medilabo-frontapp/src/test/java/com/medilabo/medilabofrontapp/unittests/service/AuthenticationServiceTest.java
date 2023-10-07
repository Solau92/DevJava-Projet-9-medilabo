package com.medilabo.medilabofrontapp.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.AuthenticationProxy;
import com.medilabo.medilabofrontapp.service.implementation.AuthenticationServiceImpl;

@SpringBootTest
class AuthenticationServiceTest {
	
	@InjectMocks
	AuthenticationServiceImpl authenticationService;
	
	@Mock
	AuthenticationProxy authenticationProxy;
	
	@Mock
	Context context;
	
	User loggedUser;
	String header;
	
	@BeforeEach
	void setUp() {

		loggedUser = new User();
		loggedUser.setUsername("username");
		loggedUser.setPassword("password");

		header = "ok";
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
		when(context.getLoggedUser()).thenReturn(new User());
		when(context.getRedirectAfterExceptionUrl()).thenReturn(HTMLPage.HOME);
		
		// WHEN 
		authenticationService.login(header, loggedUser);
		String returnUrl = context.getRedirectAfterExceptionUrl();
		
		// THEN 
		assertEquals(returnUrl, HTMLPage.HOME);		
	}

}
