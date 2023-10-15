package com.medilabo.medilabofrontapp.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.service.NoteService;

import feign.FeignException;

@Service
public class NoteServiceImpl implements NoteService {

	private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

	private MicroserviceNoteProxy noteProxy;

	private static Context context;

	public NoteServiceImpl(MicroserviceNoteProxy noteProxy, Context context) {
		this.noteProxy = noteProxy;
		this.context = context;
	}

	/**
	 * Adds the given note in the note app and database.
	 * 
	 * @param header corresponding to authorization header
	 * 
	 * @param note
	 * @return NoteBean corresponding to the note added
	 */
	@Override
	public NoteBean addNote(String header, NoteBean note) {

		NoteBean noteAdded = new NoteBean();

		try {
			note.setPatientId(context.getPatientId());
			log.info("patientId in addNote : {}", note.getPatientId());
			noteAdded = noteProxy.addNote(header, note);
			log.info("Note saved id : {}, content : {}", noteAdded.getId(), noteAdded.getContent());
			context.resetUrl();
			context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());

		} catch (FeignException e) {
			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				context.setRedirectAfterExceptionUrl("/note/add");
				context.setReturnUrl(Redirect.HOME);
			}
		}
		return noteAdded;
	}

	/**
	 * Returns a note, given its id, from the note app and database.
	 * 
	 * @param header corresponding to authorization header
	 * @param id
	 * @return NoteBean corresponding to the note found
	 */
	@Override
	public NoteBean getNote(String header, String id) {
		
		NoteBean note = new NoteBean();
		
		try {
			note = noteProxy.getNote(header, id);
			log.info("Note found id : {}, content : {}", note.getId(), note.getContent());

		} catch(FeignException e) {
			log.info("FeignException status : {} message : {}", e.status(), e.getMessage());
			context.setMessage(e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception 401", e.getMessage());
				context.setRedirectAfterExceptionUrl("/patient/validateUpdate/" + context.getPatientId());
				context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());
			}
		}		
		return note;
	}

	/**
	 * Returns the list of all the notes corresponding to a patient given his id,
	 * from the note app and database.
	 * 
	 * @param header    corresponding to authorization header
	 * @param patientId
	 * @return List<NoteBean> corresponding to the notes found
	 */
	@Override
	public List<NoteBean> getNotes(String header, int patientId) {
		
		List<NoteBean> notes = new ArrayList<>();
		
		try {
			notes = noteProxy.getNotes(header, patientId);
			log.debug(notes.toString());

		} catch(FeignException e) {
			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());
			context.setMessage(e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception 401 {}", e.getMessage());
				context.setRedirectAfterExceptionUrl("/patient/validateUpdate/" + context.getPatientId());
				context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());
			}
		}		 
		return notes;
	}

	/**
	 * Updates the given note in the note app and database.
	 * 
	 * @param header corresponding to authorization header
	 * @param note
	 * @return NoteBean corresponding to the note updated
	 */
	@Override
	public NoteBean updateNote(String header, NoteBean note) {

		NoteBean noteUpdated = new NoteBean();

		try {
			note.setPatientId(context.getPatientId());
			noteUpdated = noteProxy.updateNote(header, note);
			log.info("Note updated id : {}, content : {}", noteUpdated.getId(), noteUpdated.getContent());
			context.resetUrl();
			context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());

		} catch (FeignException e) {
			log.info("FeignException status : {} message : {}", e.status(), e.getMessage());
			context.setMessage(e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception 401", e.getMessage());
				context.setRedirectAfterExceptionUrl("/patient/validateUpdate/" + context.getPatientId());
				context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());
			}
		}
		return noteUpdated;
	}

	/**
	 * Deletes the given note in the note app and database.
	 * 
	 * @param header corresponding to authorization header
	 * @param note   corresponding to the note to delete
	 */
	@Override
	public void deleteNote(String header, NoteBean note) {

		try {
			noteProxy.deleteNote(header, note);
			log.info("Note deleted id : {}, content : {}", note.getId(), note.getContent());
			context.resetUrl();
			context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());

		} catch (FeignException e) {
			log.info("FeignException status : {} message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				log.info("Exception 401", e.getMessage());
				context.setRedirectAfterExceptionUrl("/patient/patients");
				context.setReturnUrl(Redirect.HOME);
			}
		}
	}

}
