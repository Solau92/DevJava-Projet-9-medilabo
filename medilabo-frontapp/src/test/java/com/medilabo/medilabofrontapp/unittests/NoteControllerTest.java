package com.medilabo.medilabofrontapp.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.controller.NoteController;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;

import feign.FeignException;

@SpringBootTest
class NoteControllerTest {

	@InjectMocks
	private NoteController noteController;

	@Mock
	private MicroservicePatientProxy patientProxy;

	@Mock
	private MicroserviceNoteProxy noteProxy;

	@Mock
	private Context context;

	@Mock
	private Model model;

	@Mock
	private BindingResult bResult;
	
	@Mock
	private NoteBean noteMock;
	
	User loggedUser;
	User notLoggedUser;
	String header;
	String wrongHeader;
	NoteBean note1;

	@BeforeEach
	void setUp() {

		loggedUser = new User();
		loggedUser.setUsername("username");
		loggedUser.setPassword("password");

		notLoggedUser = new User();
		notLoggedUser.setUsername(null);
		notLoggedUser.setPassword("password");
		
		header = "ok";
		wrongHeader = "notOk";
		
		note1 = new NoteBean();
		note1.setId("111111");
		note1.setDate(LocalDate.now());
		note1.setContent("content1");
	}

	@Test
	void addNoteForm_Ok_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);
		when(context.setAuthHeader()).thenReturn(header);

		// WHEN
		String result = noteController.addNoteForm(1, model);

		// THEN
		assertEquals("addNote", result);
	}

	@Test
	void addNoteForm_NotLogged_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);
		when(context.setAuthHeader()).thenReturn(wrongHeader);

		// WHEN
		String result = noteController.addNoteForm(1, model);

		// THEN
		assertEquals("redirect:/", result);
	}
	
	@Test
	void validateNote_Ok_Test() {

		// GIVEN
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getPatientId()).thenReturn(1);
		when(noteProxy.addNote(any(String.class), any(NoteBean.class))).thenReturn(note1);

		// WHEN
		String result = noteController.validateNote(note1, bResult, model);

		// THEN
		assertEquals("redirect:/patient/view/1", result);
	}
	
	@Test
	void validateNote_ResultHasError_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(true);

		// WHEN
		String result = noteController.validateNote(note1, bResult, model);

		// THEN
		assertEquals("addNote", result);
	}
	
	////////////////
	@Disabled
	@Test
	void validateNote_Forbidden_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);
		when(context.getPatientId()).thenReturn(1);
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(noteProxy.addNote(any(String.class), any(NoteBean.class))).thenThrow(FeignException.class);

		// WHEN
		String result = noteController.validateNote(note1, bResult, model);

		// THEN
		assertEquals("redirect:/", result);
	}
	
	@Test
	void updateNoteForm_Ok_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);
		when(context.setAuthHeader()).thenReturn(header);
		when(noteProxy.getNote(any(String.class), any(String.class))).thenReturn(note1);

		// WHEN
		String result = noteController.updateNoteForm("111111", model);

		// THEN
		assertEquals("updateNote", result);
	}
	
	@Test
	void updateNoteForm_Forbidden_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);

		// WHEN
		String result = noteController.updateNoteForm("111111", model);

		// THEN
		assertEquals("redirect:/", result);
	}
	
	@Test
	void updateNote_Ok_Test() {
		
		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getPatientId()).thenReturn(1);
		when(noteProxy.updateNote(any(String.class), any(NoteBean.class))).thenReturn(note1);

		// WHEN
		String result = noteController.updateNote("111111", note1, bResult, model);

		// THEN
		assertEquals("redirect:/patient/view/1", result);
	}
	
	@Test
	void updateNote_ResultHasError_Test() {
		
		// GIVEN
		when(bResult.hasErrors()).thenReturn(true);

		// WHEN
		String result = noteController.updateNote("111111", note1, bResult, model);

		// THEN
		assertEquals("updateNote", result);
	}
	
	/////////////////
	@Disabled
	@Test
	void updateNote_Forbidden_Test() {
		
		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getPatientId()).thenReturn(1);
		when(noteProxy.updateNote(any(String.class), any(NoteBean.class))).thenThrow(FeignException.class);

		// WHEN
		String result = noteController.updateNote("111111", note1, bResult, model);

		// THEN
		assertEquals("updatePatient", result);
	}
	
	@Test
	void deleteNote_Ok_Test() {
		
		// GIVEN 
		when(context.setAuthHeader()).thenReturn(header);
		when(noteMock.getPatientId()).thenReturn(2);
		when(context.getPatientId()).thenReturn(2);

		// WHEN
		String result = noteController.deleteNote("111111", note1, bResult, model);

		// THEN
		assertEquals("redirect:/patient/view/2", result);
	}
}
