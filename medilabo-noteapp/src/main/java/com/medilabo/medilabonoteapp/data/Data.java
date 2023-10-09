package com.medilabo.medilabonoteapp.data;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;
import com.medilabo.medilabonoteapp.service.NoteService;

import jakarta.annotation.PostConstruct;

@Component
@Profile("dev")
public class Data {
	
	private static final Logger log = LoggerFactory.getLogger(Data.class);

	private NoteService noteService;
	
	public Data(NoteService noteService) {
		this.noteService = noteService;	
	}
	
	@PostConstruct
	private void insertData() throws NoteNotFoundException {
		
		List<Note> notesPatient1 = noteService.findByPatientId(1);		
		for(Note n : notesPatient1) {
			noteService.delete(n);
		}
		
		List<Note> notesPatient2 = noteService.findByPatientId(2);		
		for(Note n : notesPatient2) {
			noteService.delete(n);
		}
		
		List<Note> notesPatient3 = noteService.findByPatientId(3);		
		for(Note n : notesPatient3) {
			noteService.delete(n);
		}
		
		List<Note> notesPatient4 = noteService.findByPatientId(4);		
		for(Note n : notesPatient4) {
			noteService.delete(n);
		}
				
		Note note11 = new Note(1, LocalDate.now(), "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé");
		noteService.save(note11);
		log.info("Dans Data : save note 11");
		
		Note note21 = new Note(2, LocalDate.now(), "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement");
		noteService.save(note21);
		
		Note note22 = new Note(2, LocalDate.now(), "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale");
		noteService.save(note22);
		
		Note note31 = new Note(3, LocalDate.now(), "Le patient déclare qu'il fume depuis peu");
		noteService.save(note31);
	
		Note note32 = new Note(3, LocalDate.now(), "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé");
		noteService.save(note32);
		
		Note note41 = new Note(4, LocalDate.now(), "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments");
		noteService.save(note41);
		
		Note note42 = new Note(4, LocalDate.now(), "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps");
		noteService.save(note42);
		
		Note note43 = new Note(4, LocalDate.now(), "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé");
		noteService.save(note43);
		
		Note note44 = new Note(4, LocalDate.now(), "Taille, Poids, Cholestérol, Vertige et Réaction");
		noteService.save(note44);

	}
	
}
