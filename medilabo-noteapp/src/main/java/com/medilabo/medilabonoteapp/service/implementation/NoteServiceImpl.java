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
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public List<Note> findByPatientId(int id) {
		return noteRepository.findByPatientId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws NoteNotFoundException
	 */
	@Override
	public Note findById(String id) throws NoteNotFoundException {
		
		Optional<Note> optionalNote = noteRepository.findById(id);
		
		if(optionalNote.isEmpty()) {
			throw new NoteNotFoundException("Note for patient with id " + id + " not found");
		}		
		return optionalNote.get();
	}

	/**
	 * 
	 * @param note
	 * @return
	 */
	@Override
	public Note save(Note note) {
		return noteRepository.save(note);
	}
	
	/**
	 * 
	 * @param note
	 * @return
	 * @throws NoteNotFoundException 
	 */
	public Note update(Note note) throws NoteNotFoundException {
		
		Optional<Note> optionalNote = noteRepository.findById(note.getId());
		
		if(optionalNote.isEmpty()) {
			throw new NoteNotFoundException("Patient with id " + note.getId() + " not found");
		}		

		return noteRepository.save(note);
	}
	
	/**
	 * 
	 * @param note
	 * @throws NoteNotFoundException
	 */
	@Override
	public void delete(Note note) throws NoteNotFoundException {
		
		Optional<Note> optionalNote = noteRepository.findById(note.getId());
		
		if(optionalNote.isEmpty()) {
			throw new NoteNotFoundException("Patient with id " + note.getId() + " not found");
		}
		
		noteRepository.delete(note);
	}

}
