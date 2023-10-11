package com.medilabo.medilabofrontapp.unittests.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.controller.AuthenticationController;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.AuthenticationProxy;
import com.medilabo.medilabofrontapp.service.implementation.AuthenticationServiceImpl;

@SpringBootTest
class AuthenticationControllerTest {

	@InjectMocks
	AuthenticationController authenticationController;

	@Mock
	Context context;

	@Mock
	AuthenticationServiceImpl authenticationService;
	
	@Mock
	AuthenticationProxy authenticationProxy;

	@Mock
	Model model;

	@Mock
	BindingResult bResult;

	@Mock
	User user;

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
	void loginForm_Ok_Test() {

		// GIVEN
		// WHEN
		String result = authenticationController.loginForm(model);

		// THEN
		assertEquals(HTMLPage.LOGIN, result);
	}

	@Test
	void login_Ok_Test() {
		
		// GIVEN 
		when(bResult.hasErrors()).thenReturn(false);
		when(context.getReturnUrl()).thenReturn(HTMLPage.INDEX);

		// WHEN 
		String result = authenticationController.login(new User(), bResult, model);
		
		// THEN 
		verify(authenticationService, times(1)).login(any(String.class), any(User.class));
		assertEquals(HTMLPage.INDEX, result);


	}
	
	@Test
	void login_Forbidden_Test() {
		
		// GIVEN 
		when(bResult.hasErrors()).thenReturn(false);
		when(context.getReturnUrl()).thenReturn(HTMLPage.HOME);

		// WHEN 
		String result = authenticationController.login(new User(), bResult, model);
		
		// THEN 
		assertEquals(HTMLPage.HOME, result);
	}

	@Test
	void login_ResultHasError_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(true);

		// WHEN
		String result = authenticationController.login(new User(), bResult, model);

		// THEN
		assertEquals("login", result);

	}

	@Test
	void index_Ok_Test() {

		// GIVEN
		// WHEN
		String result = authenticationController.index();

		// THEN
		assertEquals("index", result);
	}

}
