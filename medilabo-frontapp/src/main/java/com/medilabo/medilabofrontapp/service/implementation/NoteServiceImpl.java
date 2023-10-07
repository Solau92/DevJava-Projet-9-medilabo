package com.medilabo.medilabofrontapp.service.implementation;

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
public class NoteServiceImpl implements NoteService{
	
	private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

	private MicroserviceNoteProxy noteProxy;

	private static Context context;

	public NoteServiceImpl(MicroserviceNoteProxy noteProxy, Context context) {
		this.noteProxy = noteProxy;
		this.context = context;
	}

	@Override
	public NoteBean addNote(String header, NoteBean note) {

		NoteBean noteAdded = new NoteBean();
		
		try {
			note.setPatientId(context.getPatientId());
			log.info("patientId validate : " + note.getPatientId());
			noteProxy.addNote(header, note);
			context.resetUrl();
			context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());
			
		} catch(FeignException e) {	
			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setRedirectAfterExceptionUrl("/note/add");
				context.setReturnUrl(Redirect.HOME);
			}
		}		
		return noteAdded;
	}

	@Override
	public NoteBean getNote(String header, String id) {
		// TODO : gérer exceptions
		return noteProxy.getNote(header, id);
	}

	@Override
	public List<NoteBean> getNotes(String header, int patientId) {
		// TODO : gérer exceptions
		return noteProxy.getNotes(header, patientId);
	}

	@Override
	public NoteBean updateNote(String header, NoteBean note) {

		NoteBean noteUpdated = new NoteBean();
		
		try {
			note.setPatientId(context.getPatientId());
			noteProxy.updateNote(header, note);
			context.resetUrl();
			context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());
			
		} catch (FeignException e) {
			log.info("statut : {} message : {}", e.status(), e.getMessage());
			context.setMessage(e.getLocalizedMessage());

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setRedirectAfterExceptionUrl("/patient/validateUpdate/" + context.getPatientId());
				context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());
			}
		}	
		return noteUpdated;
	}
		
	@Override
	public void deleteNote(String header, NoteBean note) {

		try {
			noteProxy.deleteNote(header, note);
			context.resetUrl();
			context.setReturnUrl(Redirect.VIEW_PATIENT + context.getPatientId());
			
		} catch (FeignException e) {
			if (e.status() == 401) {
				context.setRedirectAfterExceptionUrl("/patient/patients");
				context.setReturnUrl(Redirect.HOME);
			}
		}		
	}
	
	

}
