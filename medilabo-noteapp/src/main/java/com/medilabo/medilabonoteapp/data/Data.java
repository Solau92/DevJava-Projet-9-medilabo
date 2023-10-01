package com.medilabo.medilabonoteapp.data;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.service.NoteService;

import jakarta.annotation.PostConstruct;

@Component
public class Data {
	
	private NoteService noteService;
	
	public Data(NoteService noteService) {
		this.noteService = noteService;
	}
	
	@PostConstruct
	private void insertData() {
		
		Note note11 = new Note(1, LocalDate.now().minusWeeks(1), "Hémoglobine A1C, Taille, Vertiges");
		noteService.save(note11);
		
		Note note12 = new Note(1, LocalDate.now().minusDays(1), "Anticorps");
		noteService.save(note12);
		
		Note note21 = new Note(2, LocalDate.now().minusMonths(1), "Note 2.1");
		noteService.save(note21);
		
		Note note31 = new Note(3, LocalDate.now(), "Note 3.1");
		noteService.save(note31);
	
	}
	

}
