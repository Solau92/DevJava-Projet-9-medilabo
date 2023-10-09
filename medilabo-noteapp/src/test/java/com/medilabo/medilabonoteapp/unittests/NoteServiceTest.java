package com.medilabo.medilabonoteapp.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.exception.NoteNotFoundException;
import com.medilabo.medilabonoteapp.repository.NoteRepository;
import com.medilabo.medilabonoteapp.service.implementation.NoteServiceImpl;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

	@InjectMocks
	NoteServiceImpl noteService;

	@Mock
	NoteRepository noteRepository;

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
		when(noteRepository.findByPatientId(anyInt())).thenReturn(notes);

		// WHEN
		List<Note> result = noteService.findByPatientId(1);

		// THEN
		assertTrue(result.contains(note1));
	}

	@Test
	void findById_Ok_Test() throws NoteNotFoundException {

		// GIVEN
		when(noteRepository.findById(any(String.class))).thenReturn(Optional.of(note2));

		// WHEN
		Note result = noteService.findById("222222");

		// THEN
		assertEquals(note2.getContent(), result.getContent());
	}

	@Test
	void findById_NoteNotFound_Test() throws NoteNotFoundException {

		// GIVEN
		when(noteRepository.findById(any(String.class))).thenReturn(Optional.empty());

		// WHEN
		// THEN
		assertThrows(NoteNotFoundException.class, () -> noteService.findById("xxxxx"));
	}

	@Test
	void save_Ok_Test() {

		// GIVEN
		when(noteRepository.save(any(Note.class))).thenReturn(note1);

		// WHEN
		Note result = noteService.save(note1);

		// THEN
		assertEquals(note1.getContent(), result.getContent());
	}

	@Test
	void update_Ok_Test() throws NoteNotFoundException {

		// GIVEN
		when(noteRepository.findById(any(String.class))).thenReturn(Optional.of(note1));
		note1.setContent("content 1 modified");
		when(noteRepository.save(any(Note.class))).thenReturn(note1);

		// WHEN
		Note result = noteService.update(note1);

		// THEN
		assertEquals(note1.getContent(), result.getContent());
	}

	@Test
	void update_NoteNotFound_Test() throws NoteNotFoundException {

		// GIVEN
		when(noteRepository.findById(any(String.class))).thenReturn(Optional.empty());

		// WHEN
		// THEN
		assertThrows(NoteNotFoundException.class, () -> noteService.update(note1));
	}

	@Test
	void delete_Ok_Test() throws NoteNotFoundException {

		// GIVEN
		when(noteRepository.findById(any(String.class))).thenReturn(Optional.of(note1));

		// WHEN
		noteService.delete(note1);

		// THEN
		verify(noteRepository, times(1)).delete(note1);
	}

	@Test
	void delete_NoteNotFound_Test() throws NoteNotFoundException {

		// GIVEN
		when(noteRepository.findById(any(String.class))).thenReturn(Optional.empty());

		// WHEN
		// THEN
		assertThrows(NoteNotFoundException.class, () -> noteService.delete(note1));
	}

}
