package com.medilabo.medilabofrontapp.controller;

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
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;

import feign.FeignException;
import jakarta.validation.Valid;

@Controller 
public class ClientController {

	private final MicroservicePatientProxy patientsProxy;

	private Logger log = LoggerFactory.getLogger(ClientController.class);

	public ClientController(MicroservicePatientProxy patientsProxy) {
		this.patientsProxy = patientsProxy;
	}

	@RequestMapping("/")
	public String index(Model model) {
		List<PatientBean> patients = patientsProxy.patients();
		model.addAttribute("patients", patients);
		return "index";
	}

	@GetMapping("/patient/add")
	public String addPatientForm(Model model) {
		PatientBean patient = new PatientBean();
		model.addAttribute("patient", patient);
		return "addPatient";
	}

	@PostMapping("/patient/validate")
	public String validate(@Valid @ModelAttribute("patient") PatientBean patient, BindingResult result, Model model) {

		if(result.hasErrors()) {
			log.error("error !!");
			return "addPatient";
		}

		log.info("ClientController result has not error");
		PatientBean patientSaved = patientsProxy.addPatient(patient);
		return "redirect:/";
	}

	@GetMapping("/patient/update/{id}")
	public String updatePatientForm(@PathVariable int id, Model model) {
		PatientBean patient = patientsProxy.getPatient(id);
		model.addAttribute("patient", patient);
		return "updatePatient";
	}

	@PostMapping("/patient/validateUpdate/{id}")
	public String updatePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient, BindingResult result, Model model) {

		if(result.hasErrors()) {
			log.error("error !!");
			return "updatePatient";
		}

		log.info("ClientController result has not error");
		patient.setId(id);
		try {
			PatientBean patientUpdated = patientsProxy.updatePatient(patient);
		} catch (FeignException e) {
			log.info(e.getMessage());
			model.addAttribute(("error"), e.responseBody());
			return "updatePatient";
		}
		return "redirect:/";
	}

	@GetMapping("/patient/delete/{id}")
	public String deletePatient (@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient, BindingResult result, Model model) {

		patientsProxy.deletePatient(patient);
		return "redirect:/";
	}
}
