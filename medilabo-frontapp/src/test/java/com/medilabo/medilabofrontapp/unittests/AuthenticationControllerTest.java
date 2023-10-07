package com.medilabo.medilabofrontapp.unittests;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.controller.AuthenticationController;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.service.implementation.AuthenticationServiceImpl;

@SpringBootTest
class AuthenticationControllerTest {

	@InjectMocks
	private AuthenticationController authenticationController;

	@Mock
	private Context context;

	@Mock
	private AuthenticationServiceImpl authenticationService;

	@Mock
	private Model model;

	@Mock
	private BindingResult bResult;

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
		assertEquals("login", result);
	}

	@Disabled
	@Test
	void login_Ok_Test() {
		
		// GIVEN 
		when(bResult.hasErrors()).thenReturn(false);
		when(authenticationService.login(any(String.class), any(User.class))).thenReturn("/index");

		// WHEN 
		String result = authenticationController.login(new User(), bResult, model);
		
		// THEN 
		assertEquals("/index", result);
	}
	
	/// TODO
	@Disabled
	@Test
	void login_Forbidden_Test() {
		
		// GIVEN 
		when(bResult.hasErrors()).thenReturn(false);
		when(authenticationService.login(any(String.class), any(User.class))).thenReturn("/");

		// WHEN 
		String result = authenticationController.login(new User(), bResult, model);
		
		// THEN 
		assertEquals("/", result);
	}

	@Disabled
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
