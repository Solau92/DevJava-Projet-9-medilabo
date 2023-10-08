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

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;
import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.service.implementation.PatientServiceImpl;
import com.medilabo.medilabofrontapp.service.implementation.RiskServiceImpl;
import com.medilabo.medilabofrontapp.service.implementation.NoteServiceImpl;

import jakarta.validation.Valid;

@Controller
public class PatientController {

	private static final Logger log = LoggerFactory.getLogger(PatientController.class);

	private static Context context;

	private PatientServiceImpl patientService;

	private NoteServiceImpl noteService;
	
	private RiskServiceImpl riskService;
	
	public PatientController(PatientServiceImpl patientService, NoteServiceImpl noteService,
			RiskServiceImpl riskService, Context context) {
		this.patientService = patientService;
		this.noteService = noteService;
		this.riskService = riskService;
		this.context = context;
	}

	/**
	 * Returns a HTML page with a list of all the users.
	 * 
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@RequestMapping("/patient/patients")
	public String patientList(Model model) {

		String authHeader = context.setAuthHeader();
		log.info("Authorization header : {}", authHeader);

		List<PatientBean> patients = patientService.getPatients(authHeader);
		model.addAttribute("patients", patients);
		return context.getReturnUrl();
	}

	/**
	 * Returns a HTML page with informations of the user, given his id.
	 * 
	 * @param id of the user
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@GetMapping("/patient/view/{id}")
	public String viewPatient(@PathVariable int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.error("Logged User is null in viewPatient");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		PatientBean patient = patientService.getPatient(authHeader, id);
		model.addAttribute("patient", patient);
		log.info("getPatient {} in viewPatient", patient);

		List<NoteBean> notes = noteService.getNotes(authHeader, id);
		model.addAttribute("notes", notes);
		log.info("getNotes from patient {} in viewPatient", patient.getId());

		DiabetesRiskBean risk = riskService.getDiabetesRisk(authHeader, id);
		model.addAttribute("risk", risk);
		log.info("getDiabetesRisk from patient {} in viewPatient", patient.getId());

		return context.getReturnUrl();
	}

	/**
	 * Returns a HTML page with fields to create a new patient.
	 * 
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@GetMapping("/patient/add")
	public String addPatientForm(Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.error("Logged User is null in addPatientForm");
			context.setRedirectAfterExceptionUrl("/patient/add");
			return Redirect.HOME;
		}

		model.addAttribute("user", context.getLoggedUser());
		PatientBean patient = new PatientBean();
		model.addAttribute("patient", patient);
		return HTMLPage.ADD_PATIENT;
	}

	/**
	 * Validates the new patient and returns a HTML page on which the user will be redirected.
	 * 
	 * @param patient that must be added
	 * @param result
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@PostMapping("/patient/validate")
	public String validate(@Valid @ModelAttribute("patient") PatientBean patient, BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in validate");
			return HTMLPage.ADD_PATIENT;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		patientService.addPatient(authHeader, patient);
		model.addAttribute("error", context.getMessage());
		return context.getReturnUrl();
	}

	/**
	 * Returns a HTML page with fields filled with the patient information, that can be modified, given the id of the patient.
	 * 
	 * @param id of the patient
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@GetMapping("/patient/update/{id}")
	public String updatePatientForm(@PathVariable int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.error("Logged User is null in updatePatientForm");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		PatientBean patient = patientService.getPatient(authHeader, id);
		model.addAttribute("patient", patient);
		return HTMLPage.UPDATE_PATIENT;
	}

	/**
	 * Validates the modification of the patient given its id, and returns a HTML page on which the user will be redirected.
	 * 
	 * @param id of the patient
	 * @param patient that must be modified
	 * @param result
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@PostMapping("/patient/validateUpdate/{id}")
	public String updatePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in updatePatient");
			return HTMLPage.UPDATE_PATIENT;
		}

		patient.setId(id);
		
		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		patientService.updatePatient(authHeader, patient);
		model.addAttribute("error", context.getMessage());
		return context.getReturnUrl();

	}

	/**
	 * Removes a patient given the patient and its id and returns a HTML page on which the user will be redirected.
	 * 
	 * @param id of the patient
	 * @param patient that must be deleted
	 * @param result
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@GetMapping("/patient/delete/{id}")
	public String deletePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient,
			BindingResult result, Model model) {

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		patientService.deletePatient(authHeader, patient);
		return context.getReturnUrl();
	}

}
