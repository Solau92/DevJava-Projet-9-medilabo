package com.medilabo.medilabonoteapp.service;

import java.util.List;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;

public interface NoteService {
	
	/**
	 * Returns the notes concerning a patient, given his id. 
	 * 
	 * @param id
	 * @return List<Note>
	 */
	List<Note> findByPatientId(int id);

	/**
	 * Returns a note, given its id. 
	 * 
	 * @param id
	 * @return Note
	 * @throws NoteNotFoundException if the note was not found
	 */
	Note findById(String id) throws NoteNotFoundException;

	/**
	 * Saves the given note. 
	 * 
	 * @param note
	 * @return Note the note saved
	 */
	Note save(Note note);

	/**
	 * Updates the given note. 
	 * 
	 * @param note
	 * @return Note the note to update
	 * @throws NoteNotFoundException if the note was not found
	 */
	public Note update(Note note) throws NoteNotFoundException;
	
	/**
	 * Deletes the given note. 
	 * 
	 * @param note
	 * @throws NoteNotFoundException if the note was not found
	 */
	void delete(Note note) throws NoteNotFoundException;
	
}
