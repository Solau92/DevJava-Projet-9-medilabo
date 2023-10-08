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
import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.service.NoteService;

import jakarta.validation.Valid;

@Controller
public class NoteController {

	private static final Logger log = LoggerFactory.getLogger(NoteController.class);
	
	private static Context context;
	
	private final NoteService noteService;

	public NoteController(NoteService noteService, Context context) {
		this.noteService = noteService;
		this.context = context;
	}
	
	/**
	 * Returns a HTML page with informations of the note, given its id.
	 * 
	 * @param id of the note
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@GetMapping("/note/view/{id}")
	public String viewNote(@PathVariable String id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.error("Logged User is null in viewNote");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);
		
		NoteBean note = noteService.getNote(authHeader, id);
		model.addAttribute("note", note);
		return HTMLPage.VIEW_NOTE;
	} 
	
	/**
	 * Returns a HTML page with fields to enter a new note, given the patient id to whom the note is related.
	 * 
	 * @param id of the patient corresponding to the new note
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@GetMapping("/note/add/{patientId}")
	public String addNoteForm(@PathVariable("patientId") int id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.error("Logged User is null in addNoteForm");
			context.setRedirectAfterExceptionUrl("/note/add");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		model.addAttribute("user", context.getLoggedUser());
		NoteBean note = new NoteBean();
		model.addAttribute("note", note);
		return HTMLPage.ADD_NOTE;
	}
	
	/**
	 * Validates the new note and returns a HTML page on which the user will be redirected.
	 * 
	 * @param note that must be added
	 * @param result
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@PostMapping("/note/validate")
	public String validateNote(@Valid @ModelAttribute("note") NoteBean note, BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in validateNote");
			return HTMLPage.ADD_NOTE;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		noteService.addNote(authHeader, note);
		return context.getReturnUrl();
	}
	
	/**
	 * Returns a HTML page with fields filled with the note information, that can be modified, given the id of the note.
	 * 
	 * @param id of the note
	 * @param model
	 * @return a String corresponding to the HTML page 
	 */
	@GetMapping("/note/update/{id}")
	public String updateNoteForm(@PathVariable String id, Model model) {

		if (context.getLoggedUser().getUsername() == null || context.getLoggedUser().getPassword() == null) {
			log.error("Logged User is null in updateNoteForm");
			return Redirect.HOME;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		NoteBean note = noteService.getNote(authHeader, id);
		model.addAttribute("note", note);
		return HTMLPage.UPDATE_NOTE;
	}
	
	/**
	 * Validates the modification of the note given its id, and returns a HTML page on which the user will be redirected.
	 * 
	 * @param id of the note
	 * @param note that must be modified
	 * @param result
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@PostMapping("/note/validateUpdate/{id}")
	public String updateNote(@PathVariable("id") String id, @Valid @ModelAttribute("note") NoteBean note,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			log.error("Result has error in updateNote");
			return HTMLPage.UPDATE_NOTE;
		}

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		noteService.updateNote(authHeader, note);
		model.addAttribute("error", context.getMessage());
		return context.getReturnUrl();
	}
	
	/**
	 * Removes a note given the note and its id and returns a HTML page on which the user will be redirected.
	 * 
	 * @param id of the note
	 * @param note that must be deleted
	 * @param result
	 * @param model
	 * @return a String corresponding to the HTML page
	 */
	@GetMapping("/note/delete/{id}")
	public String deleteNote(@PathVariable("id") String id, @Valid @ModelAttribute("note") NoteBean note,
			BindingResult result, Model model) {

		String authHeader = context.setAuthHeader();
		log.debug("Authorization header : {}", authHeader);

		noteService.deleteNote(authHeader, note);		
		return context.getReturnUrl();
	}

}
