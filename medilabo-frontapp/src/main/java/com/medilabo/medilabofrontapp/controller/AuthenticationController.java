package com.medilabo.medilabofrontapp.controller;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.AuthenticationProxy;
import com.medilabo.medilabofrontapp.service.AuthenticationService;

import feign.FeignException;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	private static Context context;

	private AuthenticationService authenticationService;

//	private final AuthenticationProxy authenticationProxy;

//	public AuthenticationController(AuthenticationProxy authenticationProxy, Context context) {
//		this.authenticationProxy = authenticationProxy;
//		this.context = context;
//	}

	public AuthenticationController(AuthenticationService authenticationService, Context context) {
		this.authenticationService = authenticationService;
		this.context = context;
	}

	@GetMapping("/")
	public String loginForm(Model model) {
		model.addAttribute("user", context.getLoggedUser());
		return "login";
	}

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in login");
			return "login";
		}

		byte[] encodedBytes = Base64.getEncoder().encode((user.getUsername() + ":" + user.getPassword()).getBytes());
		String authHeader = "Basic " + new String(encodedBytes);
		log.info("authHeader in login : {}", authHeader);

		return authenticationService.login(authHeader, user);
	}

//	@PostMapping("/login")
//	public String login(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
//
//		if (result.hasErrors()) {
//			log.error("Result has error in login");
//			return "login";
//		}
//
//		byte[] encodedBytes = Base64.getEncoder().encode((user.getUsername() + ":" + user.getPassword()).getBytes());
//		String authHeader = "Basic " + new String(encodedBytes);
//		log.info("authHeader in login : {}", authHeader);
//
//		try {
//			authenticationProxy.login(authHeader);
//			context.getLoggedUser().setUsername(user.getUsername());
//			context.getLoggedUser().setPassword(user.getPassword());
//
//		} catch (FeignException e) {
//			log.info("Exception status login : {}", e.status());
//			if (e.status() == 401) {
//				context.setUrl("/");
//			}
//		}
//
//		String urlTempo = context.getUrl();
//		context.resetUrl();
//		return "redirect:" + urlTempo;
//	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

}
