package com.medilabo.medilabonoteapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;
import com.medilabo.medilabonoteapp.service.NoteService;

@Controller
public class NoteController {
	
	private NoteService noteService;
	
	private static final Logger log = LoggerFactory.getLogger(NoteController.class);

	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}
	
	/**
	 * 
	 * @param patientId
	 * @return
	 */
	@GetMapping("/note/notes/{patientId}")
	public ResponseEntity<List<Note>> findByPatientId(@PathVariable int patientId, @RequestHeader("Authorization") String header) {
		log.info("/note/notes/{} : Getting the list of all notes for patient {}", patientId, patientId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(noteService.findByPatientId(patientId));
	}
	
	/**
	 * 
	 * @param note
	 * @return
	 */
	@PostMapping("/note/validate")
	public ResponseEntity<Note> addNote(@RequestBody Note note, @RequestHeader("Authorization") String header){
		log.info("/note/validate : Adding note {} for patient {}", note.getContent(), note.getPatientId());
		return ResponseEntity.status(HttpStatus.CREATED).body(noteService.save(note));
	}
	
	/**
	 * 
	 * @param note
	 * @return
	 * @throws NoteNotFoundException
	 */
	@PostMapping("/note/validateUpdate")
	public ResponseEntity<Note> updateNote(@RequestBody Note note, @RequestHeader("Authorization") String header) throws NoteNotFoundException {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(noteService.update(note));
	}
	
	/**
	 * 
	 * @param note
	 * @return
	 * @throws NoteNotFoundException
	 */
	@DeleteMapping("/note/delete")
	public ResponseEntity<Void> deleteNote(@RequestBody Note note, @RequestHeader("Authorization") String header) throws NoteNotFoundException {
		log.info("/patient/delete : Deleting note {} for patient {}", note.getId(), note.getPatientId());
		noteService.delete(note);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws NoteNotFoundException
	 */
	@GetMapping("/note/{id}")
	public ResponseEntity<Note> getNote(@PathVariable String id, @RequestHeader("Authorization") String header) throws NoteNotFoundException {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(noteService.findById(id));
	}
	

}
