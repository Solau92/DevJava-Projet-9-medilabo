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
	
	
	@GetMapping("/note/add/{patientId}")
	public String addNoteForm(@PathVariable("patientId") int id, Model model) {

		if (context.loggedUser.getUsername() == null || context.loggedUser.getPassword() == null) {
			log.info("logged User null");
			context.url = "/note/add";
			return "redirect:/";
		}

		String authHeader = context.setAuthHeader();

		PatientBean patient = patientProxy.getPatient(authHeader, id);
		model.addAttribute("patient", patient);
		model.addAttribute("user", context.loggedUser);
		NoteBean note = new NoteBean();
		model.addAttribute("note", note);
		note.setPatientId(id);
		log.info("patientId note form : " + note.getPatientId());
		return "addNote";
	}

	@PostMapping("/note/validate")
	public String validateNote(@Valid @ModelAttribute("note") NoteBean note, BindingResult result, Model model) {

		log.info("patientId validate : " + note.getPatientId());

		if (result.hasErrors()) {
			log.error("Result has error in addNote");
			return "addNote";
		}

		String authHeader = context.setAuthHeader();

		try {
			noteProxy.addNote(authHeader, note);
			context.resetUrl();
			return "redirect:/patient/view/" + context.patientId;
		} catch (FeignException e) {

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.url = "/note/add";
				return "redirect:/";
			}
			return "addNote";
		}
	}

	@GetMapping("/note/update/{id}")
	public String updateNotetForm(@PathVariable String id, Model model) {

		if (context.loggedUser.getUsername() == null || context.loggedUser.getPassword() == null) {
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

		note.setId(id);

		String authHeader = context.setAuthHeader();

		try {
			log.info("before updating note");
			noteProxy.updateNote(authHeader, note);
			log.info("after updating note");
			context.resetUrl();
			return "redirect:/patient/view/" + context.patientId;
		} catch (FeignException e) {

			log.info("statut : {} message : {}", e.status(), e.getMessage());
			model.addAttribute(("error"), e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.url = "/patient/validateUpdate/" + context.patientId;
				return "updatePatient";
			}

		}
		return "redirect:/patient/view/" + context.patientId;
	}

	@GetMapping("/note/delete/{id}")
	public String deleteNote(@PathVariable("id") String id, @Valid @ModelAttribute("note") NoteBean note,
			BindingResult result, Model model) {

		String authHeader = context.setAuthHeader();

		log.info(note.toString());
		int patientId = note.getPatientId();

		try {
			noteProxy.deleteNote(authHeader, note);
			context.resetUrl();
		} catch (FeignException e) {
			if (e.status() == 401) {
				context.url = "/patient/patients";
				return "redirect:/";
			}
		}
		return "redirect:/patient/view/" + context.patientId; // A revoir, pas bon
	}

}
