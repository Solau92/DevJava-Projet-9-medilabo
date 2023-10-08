package com.medilabo.medilabofrontapp.service;

import java.util.List;

import com.medilabo.medilabofrontapp.bean.NoteBean;

public interface NoteService {

	/**
	 * Adds the given note. 
	 * 
	 * @param header corresponding to authorization header
	 * 
	 * @param note
	 * @return NoteBean corresponding to the note added
	 */
	NoteBean addNote(String header, NoteBean note);

	/**
	 * Returns a note, given its id.
	 * 
	 * @param header corresponding to authorization header
	 * @param id
	 * @return NoteBean corresponding to the note found
	 */
	NoteBean getNote(String header, String id);
	
	/**
	 * Returns the list of all the notes corresponding to a patient given his id.
	 * 
	 * @param header corresponding to authorization header
	 * @param patientId
	 * @return List<NoteBean> corresponding to the notes found 
	 */
	List<NoteBean> getNotes(String header, int patientId);

	/**
	 * Updates the given note. 
	 * 
	 * @param header corresponding to authorization header
	 * @param note 
	 * @return NoteBean corresponding to the note updated
	 */
	NoteBean updateNote(String header, NoteBean note);
	
	/**
	 * Deletes the given note. 
	 * 
	 * @param header corresponding to authorization header
	 * @param note corresponding to the note to delete
	 */
	void deleteNote(String header, NoteBean note);
}
