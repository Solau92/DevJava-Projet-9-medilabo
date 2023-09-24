package com.medilabo.medilabonoteapp.service;

import java.util.List;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;

public interface NoteService {
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<Note> findByPatientId(int id);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws NoteNotFoundException
	 */
	Note findById(String id) throws NoteNotFoundException;

	/**
	 * 
	 * @param note
	 * @return
	 */
	Note save(Note note);

	/**
	 * 
	 * @param note
	 * @return
	 * @throws NoteNotFoundException
	 */
	public Note update(Note note) throws NoteNotFoundException;
	
	/**
	 * 
	 * @param note
	 * @throws NoteNotFoundException
	 */
	void delete(Note note) throws NoteNotFoundException;
	
}
