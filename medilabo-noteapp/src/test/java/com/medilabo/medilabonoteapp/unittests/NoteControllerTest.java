package com.medilabo.medilabonoteapp.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medilabo.medilabonoteapp.controller.NoteController;
import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;
import com.medilabo.medilabonoteapp.service.implementation.NoteServiceImpl;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

	@InjectMocks
	NoteController noteController;

	@Mock
	NoteServiceImpl noteService;

	List<Note> notes;
	Note note1;
	Note note2;

	@BeforeEach
	void setUp() {

		note1 = new Note();
		note1.setId("111111");
		note1.setDate(LocalDate.now());
		note1.setContent("content 1");
		note1.setPatientId(1);

		note2 = new Note();
		note2.setId("222222");
		note2.setDate(LocalDate.now());
		note2.setContent("content 2");
		note2.setPatientId(1);

		notes = new ArrayList<>();
		notes.add(note1);
		notes.add(note2);
	}

	@Test
	void findByPatientId_Ok_Test() {

		// GIVEN
		when(noteService.findByPatientId(anyInt())).thenReturn(notes);

		// WHEN
		ResponseEntity<List<Note>> result = noteController.findByPatientId(1);

		// THEN
		assertTrue(result.getBody().contains(note2));
		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
	}
	
	@Test
	void addNote_Ok_Test() {

		// GIVEN
		when(noteService.save(any(Note.class))).thenReturn(note1);

		// WHEN
		ResponseEntity<Note> result = noteController.addNote(note1);

		// THEN
		assertEquals(note1.getContent(), result.getBody().getContent());
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}
	
	@Test
	void updateNote_Ok_Test() throws NoteNotFoundException {

		// GIVEN
		note1.setContent("content 1 modified");
		when(noteService.update(any(Note.class))).thenReturn(note1);

		// WHEN
		ResponseEntity<Note> result = noteController.updateNote(note1);

		// THEN
		assertEquals(note1.getContent(), result.getBody().getContent());
		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
	}
	
	@Test
	void deletNote_Ok_Test() throws NoteNotFoundException {

		// GIVEN

		// WHEN
		ResponseEntity<Void> result = noteController.deleteNote(note1);

		// THEN
		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
	}
	
	@Test
	void getNote_Ok_Test() throws NoteNotFoundException {

		// GIVEN
		when(noteService.findById(any(String.class))).thenReturn(note1);

		// WHEN
		ResponseEntity<Note> result = noteController.getNote(note1.getId());

		// THEN
		assertEquals(note1.getContent(), result.getBody().getContent());
		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
	}
	
}
