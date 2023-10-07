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

	@RequestMapping("/patient/patients")
	public String patientList(Model model) {

		String authHeader = context.setAuthHeader();
		List<PatientBean> patients = patientService.getPatients(authHeader);
		model.addAttribute("patients", patients);
		return context.getReturnUrl();
	}

	@GetMapping("/patient/view/{id}")
	public String viewPatient(@PathVariable int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();

		PatientBean patient = patientService.getPatient(authHeader, id);
		model.addAttribute("patient", patient);

		List<NoteBean> notes = noteService.getNotes(authHeader, id);
		model.addAttribute("notes", notes);

		DiabetesRiskBean risk = riskService.getDiabetesRisk(authHeader, id);
		model.addAttribute("risk", risk);

		log.info("Patient notes : " + notes.toString());
		return context.getReturnUrl();
	}

	@GetMapping("/patient/add")
	public String addPatientForm(Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			context.setRedirectAfterExceptionUrl("/patient/add");
			return Redirect.HOME;
		}

		model.addAttribute("user", context.getLoggedUser());
		PatientBean patient = new PatientBean();
		model.addAttribute("patient", patient);
		return HTMLPage.ADD_PATIENT;
	}

	@PostMapping("/patient/validate")
	public String validate(@Valid @ModelAttribute("patient") PatientBean patient, BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in addPatient");
			return HTMLPage.ADD_PATIENT;
		}

		String authHeader = context.setAuthHeader();
		patientService.addPatient(authHeader, patient);
		model.addAttribute("error", context.getMessage());
		return context.getReturnUrl();
	}

	@GetMapping("/patient/update/{id}")
	public String updatePatientForm(@PathVariable int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		PatientBean patient = patientService.getPatient(authHeader, id);
		model.addAttribute("patient", patient);
		return HTMLPage.UPDATE_PATIENT;
	}

	@PostMapping("/patient/validateUpdate/{id}")
	public String updatePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in updatePatient");
			return HTMLPage.UPDATE_PATIENT;
		}

		patient.setId(id);
		String authHeader = context.setAuthHeader();
		patientService.updatePatient(authHeader, patient);
		model.addAttribute("error", context.getMessage());
		return context.getReturnUrl();

	}

	@GetMapping("/patient/delete/{id}")
	public String deletePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient,
			BindingResult result, Model model) {

		String authHeader = context.setAuthHeader();
		patientService.deletePatient(authHeader, patient);
		return context.getReturnUrl();
	}

}
