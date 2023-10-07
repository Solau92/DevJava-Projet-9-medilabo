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
import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilabofrontapp.service.NoteService;
import com.medilabo.medilabofrontapp.service.PatientService;

import feign.FeignException;
import jakarta.validation.Valid;

@Controller
public class NoteController {

	private static final Logger log = LoggerFactory.getLogger(NoteController.class);
	
	private static Context context;
	
	private final PatientService patientService;
	
	private final NoteService noteService;

	public NoteController(PatientService patientService, NoteService noteService, Context context) {
		this.patientService = patientService;
		this.noteService = noteService;
		this.context = context;
	}
	
	@GetMapping("/note/view/{id}")
	public String viewNote(@PathVariable String id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		NoteBean note = noteService.getNote(authHeader, id);
		model.addAttribute("note", note);
		return HTMLPage.VIEW_NOTE;
	} 
		
	@GetMapping("/note/add/{patientId}")
	public String addNoteForm(@PathVariable("patientId") int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			context.setRedirectAfterExceptionUrl("/note/add");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		
		model.addAttribute("user", context.getLoggedUser());
		NoteBean note = new NoteBean();
		model.addAttribute("note", note);
		return HTMLPage.ADD_NOTE;
	}
	
	@PostMapping("/note/validate")
	public String validateNote(@Valid @ModelAttribute("note") NoteBean note, BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in addNote");
			return HTMLPage.ADD_NOTE;
		}

		String authHeader = context.setAuthHeader();
		noteService.addNote(authHeader, note);
		return context.getReturnUrl();
	}
	
	@GetMapping("/note/update/{id}")
	public String updateNoteForm(@PathVariable String id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.info("logged User null");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		NoteBean note = noteService.getNote(authHeader, id);
		model.addAttribute("note", note);
		return HTMLPage.UPDATE_NOTE;
	}
	
	@PostMapping("/note/validateUpdate/{id}")
	public String updateNote(@PathVariable("id") String id, @Valid @ModelAttribute("note") NoteBean note,
			BindingResult result, Model model) {

		log.info("in validate update");

		if (result.hasErrors()) {
			log.error("Result has error in updatePatient");
			return HTMLPage.UPDATE_NOTE;
		}

		String authHeader = context.setAuthHeader();
		
		noteService.updateNote(authHeader, note);
		model.addAttribute("error", context.getMessage());
		return context.getReturnUrl();
	}
	
	@GetMapping("/note/delete/{id}")
	public String deleteNote(@PathVariable("id") String id, @Valid @ModelAttribute("note") NoteBean note,
			BindingResult result, Model model) {

		String authHeader = context.setAuthHeader();

		noteService.deleteNote(authHeader, note);		
		return context.getReturnUrl();

	}

}
