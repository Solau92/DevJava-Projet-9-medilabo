package com.medilabo.medilabonoteapp.service.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;
import com.medilabo.medilabonoteapp.repository.NoteRepository;
import com.medilabo.medilabonoteapp.service.NoteService;

@Service
public class NoteServiceImpl implements NoteService {

	private NoteRepository noteRepository;

	private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

	public NoteServiceImpl(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	/**
	 * Returns from database the notes concerning a patient, given his id. 
	 * 
	 * @param id
	 * @return List<Note>
	 */
	@Override
	public List<Note> findByPatientId(int id) {
		log.info("Find all notes concerning patient with id {}", id);
		return noteRepository.findByPatientId(id);
	}
	
	/**
	 * Returns the note found in database, given its id. 
	 * 
	 * @param id
	 * @return Note
	 * @throws NoteNotFoundException if the note was not found
	 */
	@Override
	public Note findById(String id) throws NoteNotFoundException {
		
		log.info("Search note whith id {}", id);
		Optional<Note> optionalNote = noteRepository.findById(id);
		
		if(optionalNote.isEmpty()) {
			log.error("Note with id {} not found", id);
			throw new NoteNotFoundException("Note for patient with id " + id + " not found");
		}		
		return optionalNote.get();
	}

	/**
	 * Saves in database the given note. 
	 * 
	 * @param note
	 * @return Note the note saved
	 */
	@Override
	public Note save(Note note) {
		log.info("Trying to save note with id {} ", note.getId());
		return noteRepository.save(note);
	}
	
	/**
	 * Updates in database the given note. 
	 * 
	 * @param note
	 * @return Note the note to update
	 * @throws NoteNotFoundException if the note was not found
	 */
	public Note update(Note note) throws NoteNotFoundException {
		
		log.info("Trying to update note {} ", note);
		Optional<Note> optionalNote = noteRepository.findById(note.getId());
		
		if(optionalNote.isEmpty()) {
			log.error("Note with id {} not found", note.getId());
			throw new NoteNotFoundException("Patient with id " + note.getId() + " not found");
		}		
		
		return noteRepository.save(note);
	}
	
	/**
	 * Deletes in database the given note. 
	 * 
	 * @param note
	 * @throws NoteNotFoundException if the note was not found
	 */
	@Override
	public void delete(Note note) throws NoteNotFoundException {
		
		log.info("Trying to update note {} ", note);
		Optional<Note> optionalNote = noteRepository.findById(note.getId());
		
		if(optionalNote.isEmpty()) {
			log.error("Note with id {} not found", note.getId());
			throw new NoteNotFoundException("Patient with id " + note.getId() + " not found");
		}
		
		noteRepository.delete(note);
	}

}
