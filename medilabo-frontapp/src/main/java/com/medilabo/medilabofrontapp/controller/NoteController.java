package com.medilabo.medilabofrontapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;

import feign.FeignException;
import jakarta.validation.Valid;

@Controller
public class NoteController {

	private static final Logger log = LoggerFactory.getLogger(NoteController.class);
	
	private static Context context;
	
	private final MicroservicePatientProxy patientProxy;
	
	private final MicroserviceNoteProxy noteProxy;
	
	public NoteController(MicroservicePatientProxy patientProxy, MicroserviceNoteProxy noteProxy, Context context) {
		this.patientProxy = patientProxy;
		this.noteProxy = noteProxy;
		this.context = context;
	}
	
	@GetMapping("/note/view/{id}")
	public String viewNote(@PathVariable String id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return "redirect:/";
		}

		String authHeader = context.setAuthHeader();
		NoteBean note = noteProxy.getNote(authHeader, id);
		model.addAttribute("note", note);
		return "viewNote";
	} 
	
	@GetMapping("/note/add/{patientId}")
	public String addNoteForm(@PathVariable("patientId") int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			context.setUrl("/note/add");
			return "redirect:/";
		}

		String authHeader = context.setAuthHeader();
		
		model.addAttribute("user", context.getLoggedUser());
		NoteBean note = new NoteBean();
		model.addAttribute("note", note);
		return "addNote";
	}

	@PostMapping("/note/validate")
	public String validateNote(@Valid @ModelAttribute("note") NoteBean note, BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in addNote");
			return "addNote";
		}

		String authHeader = context.setAuthHeader();

		try {
			note.setPatientId(context.getPatientId());
			log.info("patientId validate : " + note.getPatientId());
			noteProxy.addNote(authHeader, note);
			context.resetUrl();
			return "redirect:/patient/view/" + context.getPatientId();
		} catch (FeignException e) {

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setUrl("/note/add");
				return "redirect:/";
			}
			return "addNote";
		}
	}

	@GetMapping("/note/update/{id}")
	public String updateNoteForm(@PathVariable String id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return "redirect:/";
		}

		String authHeader = context.setAuthHeader();
		NoteBean note = noteProxy.getNote(authHeader, id);
		model.addAttribute("note", note);
		return "updateNote";
	}

	@PostMapping("/note/validateUpdate/{id}")
	public String updateNote(@PathVariable("id") String id, @Valid @ModelAttribute("note") NoteBean note,
			BindingResult result, Model model) {

		log.info("in validate update");

		if (result.hasErrors()) {
			log.error("Result has error in updatePatient");
			return "updateNote";
		}


		String authHeader = context.setAuthHeader();

		try {
			note.setPatientId(context.getPatientId());
			noteProxy.updateNote(authHeader, note);
			context.resetUrl();
			return "redirect:/patient/view/" + context.getPatientId();
		} catch (FeignException e) {

			log.info("statut : {} message : {}", e.status(), e.getMessage());
			model.addAttribute(("error"), e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setUrl("/patient/validateUpdate/" + context.getPatientId());
				return "updatePatient"; /// ?????????????? Patient ? 
			}

		}
		return "redirect:/patient/view/" + context.getPatientId();
	}

	@GetMapping("/note/delete/{id}")
	public String deleteNote(@PathVariable("id") String id, @Valid @ModelAttribute("note") NoteBean note,
			BindingResult result, Model model) {

		String authHeader = context.setAuthHeader();

		try {
			noteProxy.deleteNote(authHeader, note);
			context.resetUrl();
			return "redirect:/patient/view/" + context.getPatientId(); 
		} catch (FeignException e) {
			if (e.status() == 401) {
				context.setUrl("/patient/patients");
				return "redirect:/";
			}
		}
		return "redirect:/patient/view/" + context.getPatientId(); 
	}

}
