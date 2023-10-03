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
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilabofrontapp.proxy.MicroserviceRiskProxy;

import feign.FeignException;
import jakarta.validation.Valid;

@Controller
public class PatientController {

	private static final Logger log = LoggerFactory.getLogger(PatientController.class);
	
	private static Context context;
	
//	private PatientService patientService;
	
	private final MicroservicePatientProxy patientProxy;
	
	private final MicroserviceNoteProxy noteProxy;
	
	private final MicroserviceRiskProxy riskProxy;
	
	public PatientController(MicroservicePatientProxy patientProxy, MicroserviceNoteProxy noteProxy, MicroserviceRiskProxy riskProxy, Context context) {
		this.patientProxy = patientProxy;
		this.noteProxy = noteProxy;
		this.riskProxy = riskProxy;
		this.context = context;
	}
	
//	public PatientController(PatientService patientService, MicroserviceNoteProxy noteProxy, Context context) {
//		this.patientService = patientService;
//		this.noteProxy = noteProxy;
//		this.context = context;
//	}
	
	@RequestMapping("/patient/patients")
	public String patientList(Model model) {

		String authHeader = context.setAuthHeader();
		List<PatientBean> patients;

		try {
			patients = patientProxy.patients(authHeader);
			model.addAttribute("patients", patients);
			context.resetUrl();
			context.resetPatientId();
			return HTMLPage.PATIENTS;
			
		} catch (FeignException e) {

			log.info("Exception status : {}", e.status());

			if (e.status() == 401) {
				context.setUrl("/patient/patients");
			}
			return Redirect.HOME;
		}
	}
	
//	@RequestMapping("/patient/patients")
//	public String patientList(Model model) {
//
//		String authHeader = context.setAuthHeader();
//		List<PatientBean> patients;
//
//		try {
//			patients = patientService.getPatients(authHeader);
//			model.addAttribute("patients", patients);			
//			return "patients";
//			
//		} catch (FeignException e) {
//
//			log.info("Exception status : {}", e.status());
//
//			if (e.status() == 401) {
//				context.setUrl("/patient/patients");
//			}
//			return "redirect:/";
//		}
//	}
	
	@GetMapping("/patient/view/{id}")
	public String viewPatient(@PathVariable int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();

		context.setPatientId(id);
		
		PatientBean patient = patientProxy.getPatient(authHeader, id);
		model.addAttribute("patient", patient);

		List<NoteBean> notes = noteProxy.getNotes(authHeader, id);
		model.addAttribute("notes", notes);
		
		DiabetesRiskBean risk = riskProxy.getDiabetesRisk(authHeader, id);
		model.addAttribute("risk", risk);

		log.info("Patient notes : " + notes.toString());
		return HTMLPage.VIEW_PATIENT;

	}
	
	@GetMapping("/patient/add")
	public String addPatientForm(Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			context.setUrl("/patient/add");
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

		try {
			patientProxy.addPatient(authHeader, patient);
			context.resetUrl();
			return Redirect.PATIENTS;
		} catch (FeignException e) {

			log.info("feignException status : " + e.status() + " message : "+  e.getMessage());
			
			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setUrl("/patient/add");
				return Redirect.HOME;
			}

			if (e.status() == 400) {
				log.info("Exception status : {}", e.status());
				String message = "A patient with the same firstName, lastName and dateOfBirth already exists";
				model.addAttribute(("error"), message);
				return HTMLPage.ADD_PATIENT;
			}
			return HTMLPage.ADD_PATIENT;
		}
	}
	
	@GetMapping("/patient/update/{id}")
	public String updatePatientForm(@PathVariable int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		PatientBean patient = patientProxy.getPatient(authHeader, id);
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

		try {
			patientProxy.updatePatient(authHeader, patient);
			context.resetUrl();
			return Redirect.PATIENTS;
		} catch (FeignException e) {

			log.info("statut : {} message : {}", e.status(), e.getMessage());
			model.addAttribute(("error"), e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setUrl("/patient/validateUpdate/" + id);
				return HTMLPage.UPDATE_PATIENT;
			}

			if (e.status() == 400) {
				log.info("Exception status : {}", e.status());
				String message = "A patient with the same firstName, lastName and dateOfBirth already exists";
				model.addAttribute(("error"), message);
				return HTMLPage.UPDATE_PATIENT;
			}

		}
		return Redirect.PATIENTS;
	}

	@GetMapping("/patient/delete/{id}")
	public String deletePatient(@PathVariable("id") int id, @Valid @ModelAttribute("patient") PatientBean patient,
			BindingResult result, Model model) {

		String authHeader = context.setAuthHeader();

		try {
			patientProxy.deletePatient(authHeader, patient);
			context.resetUrl();
		} catch (FeignException e) {
			if (e.status() == 401) {
				context.setUrl("/patient/patients");
				return Redirect.HOME;
			}
		}
		return Redirect.PATIENTS;
	}
	
}
