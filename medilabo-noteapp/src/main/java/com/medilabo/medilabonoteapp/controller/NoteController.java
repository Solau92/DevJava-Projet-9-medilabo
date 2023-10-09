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
import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;
import com.medilabo.medilabonoteapp.service.NoteService;

@Controller
public class NoteController {
	
	private static final Logger log = LoggerFactory.getLogger(NoteController.class);

	private NoteService noteService;
	
	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}
	
	/**
	 * Gets the list of all the note corresponding to a patient, given his id.
	 * 
	 * @param patientId
	 * @return ResponseEntity<List<Note>> with http status ACCEPTED
	 */
	@GetMapping("/note/notes/{patientId}")
	public ResponseEntity<List<Note>> findByPatientId(@PathVariable int patientId) {
		log.info("/note/notes/{} : Getting the list of all notes for patient {}", patientId, patientId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(noteService.findByPatientId(patientId));
	}
	
	/**
	 * Adds the given note.
	 * 
	 * @param Note the note to save
	 * @return ResponseEntity<Note> with http status CREATED
	 */
	@PostMapping("/note/validate")
	public ResponseEntity<Note> addNote(@RequestBody Note note){
		log.info("/note/validate : Adding note {} for patient {}", note.getContent(), note.getPatientId());
		return ResponseEntity.status(HttpStatus.CREATED).body(noteService.save(note));
	}
	
	/**
	 * Updates the given note.
	 * 
	 * @param Note the note to update
	 * @return ResponseEntity<Note> with http status ACCEPTED
	 * @throws NoteNotFoundException if the note to update was not found 
	 */
	@PostMapping("/note/validateUpdate")
	public ResponseEntity<Note> updateNote(@RequestBody Note note) throws NoteNotFoundException {
		log.info("/note/validateUpdate : updating note with id {} for patient {}", note.getId(), note.getPatientId());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(noteService.update(note));
	}
	
	/**
	 * Deletes the given note.
	 * 
	 * @param Note the note to delete
	 * @return with http status ACCEPTED
	 * @throws NoteNotFoundException if the note to delete was not found 
	 */
	@DeleteMapping("/note/delete")
	public ResponseEntity<Void> deleteNote(@RequestBody Note note) throws NoteNotFoundException {
		log.info("/patient/delete : Deleting note {} for patient {}", note.getId(), note.getPatientId());
		noteService.delete(note);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	/**
	 * Gets a note, given its id. 
	 * 
	 * @param id
	 * @return ResponseEntity<Note> with http status ACCEPTED
	 * @throws NoteNotFoundException if the note was not found 
	 */
	@GetMapping("/note/{id}")
	public ResponseEntity<Note> getNote(@PathVariable String id) throws NoteNotFoundException {
		log.info("/note/{} : Deleting note with id {}", id, id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(noteService.findById(id));
	}
	
}
