package com.medilabo.medilabofrontapp.controller;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.AuthenticationProxy;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;

import feign.FeignException;
import jakarta.validation.Valid;

@Controller
public class ClientController {

	private final MicroservicePatientProxy patientsProxy;

	private final AuthenticationProxy authenticationProxy;

	private static final Logger log = LoggerFactory.getLogger(ClientController.class);

	private User loggedUser;

	private String url;

	public ClientController(MicroservicePatientProxy patientsProxy, AuthenticationProxy authenticationProxy) {
		this.patientsProxy = patientsProxy;
		this.authenticationProxy = authenticationProxy;
		this.loggedUser = new User();
		resetUrl();
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/")
	public String loginForm(Model model) {
		model.addAttribute("user", this.loggedUser);
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

		try {
			authenticationProxy.login(authHeader);
			this.loggedUser.setUsername(user.getUsername());
			this.loggedUser.setPassword(user.getPassword());

		} catch (FeignException e) {
			log.info("Exception status login : {}", e.status());
			if (e.status() == 401) {
				this.url = "/";
			}
		}

		String urlTempo = this.url;
		resetUrl();
		return "redirect:" + urlTempo;
	}

	@RequestMapping("/patient/patients")
	public String index(Model model) {

		String authHeader = setAuthHeader();
		List<PatientBean> patients;

		try {
			patients = patientsProxy.patients(authHeader);
			model.addAttribute("patients", patients);
			resetUrl();
			return "patients";
		} catch (FeignException e) {

			log.info("Exception status : {}", e.status());

			if (e.status() == 401) {
				this.url = "/patient/patients";
			}
			return "redirect:/";
		}
	}

	@GetMapping("/patient/add")
	public String addPatientForm(Model model) {

		if (this.loggedUser.getUsername() == null || this.loggedUser.getPassword() == null) {
			log.info("logged User null");
			this.url = "/patient/add";
			return "redirect:/";
		}

		model.addAttribute("user", this.loggedUser);
		PatientBean patient = new PatientBean();
		model.addAttribute("patient", patient);
		return "addPatient";
	}

	@PostMapping("/patient/validate")
	public String validate(@Valid @ModelAttribute("patient") PatientBean patient, BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in addPatient");
			return "addPatient";
		}

		String authHeader = setAuthHeader();

		try {
			patientsProxy.addPatient(authHeader, patient);
			resetUrl();
			return "redirect:/patient/patients";
		} catch (FeignException e) {

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				this.url = "/patient/add";
				return "redirect:/";
			}

			if (e.status() == 400) {
				log.info("Exception status : {}", e.status());
				String message = "A patient with the same firstName, lastName and dateOfBirth already exists";
				model.addAttribute(("error"), message);
				return "addPatient";
			}
			return "addPatient";
		}
	}

	@GetMapping("/patient/update/{id}")
	public String updatePatientForm(@PathVariable int id, Model model) {

		if (this.loggedUser.getUsername() == null || this.loggedUser.getPassword() == null) {
			log.info("logged User null");
			return "redirect:/";
		}

		String authHeader = setAuthHeader();
		PatientBean patient = patientsProxy.getPatient(authHeader, id);
		model.addAttribute("patient", patient);
		return "updatePatient";
	}

	@PostMapping("/patient/validateUpdate/{id}")
	public String updatePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in updatePatient");
			return "updatePatient";
		}

		patient.setId(id);

		String authHeader = setAuthHeader();

		try {
			patientsProxy.updatePatient(authHeader, patient);
			resetUrl();
			return "redirect:/patient/patients";
		} catch (FeignException e) {

			log.info("statut : {} message : {}", e.status(), e.getMessage());
			model.addAttribute(("error"), e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				this.url = "/patient/validateUpdate/" + id;
				return "updatePatient";
			}

			if (e.status() == 400) {
				log.info("Exception status : {}", e.status());
				String message = "A patient with the same firstName, lastName and dateOfBirth already exists";
				model.addAttribute(("error"), message);
				return "updatePatient";
			}

		}
		return "redirect:/patient/patients";
	}

	@GetMapping("/patient/delete/{id}")
	public String deletePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient,
			BindingResult result, Model model) {

		String authHeader = setAuthHeader();

		try {
			patientsProxy.deletePatient(authHeader, patient);
			resetUrl();
		} catch (FeignException e) {
			if (e.status() == 401) {
				this.url = "/patient/patients";
				return "redirect:/";
			}
		}
		return "redirect:/patient/patients";
	}

///////////////////////////// private methods ///////////////////////////// 

	private String setAuthHeader() {
		String username = this.loggedUser.getUsername();
		String password = this.loggedUser.getPassword();
		byte[] encodedBytes = Base64.getEncoder().encode((username + ":" + password).getBytes());
		String authHeader = "Basic " + new String(encodedBytes);
		log.info("authHeader in setAuthHeader : {}", authHeader);
		return authHeader;
	}

	private void resetUrl() {
		this.url = "/index";
	}

}
