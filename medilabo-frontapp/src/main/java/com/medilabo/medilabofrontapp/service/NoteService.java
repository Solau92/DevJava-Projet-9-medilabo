package com.medilabo.medilabofrontapp.service;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.medilabo.medilabofrontapp.bean.NoteBean;

public interface NoteService {

	NoteBean addNote(String header, NoteBean note);

	NoteBean getNote(String header, String id);
	
	List<NoteBean> getNotes(String header, int patientId);

	NoteBean updateNote(String header, NoteBean note);
	
	void deleteNote(String header, NoteBean note);
}
