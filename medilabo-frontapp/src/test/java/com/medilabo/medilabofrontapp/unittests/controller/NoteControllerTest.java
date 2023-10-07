package com.medilabo.medilabofrontapp.unittests.controller;

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
import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.controller.NoteController;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilabofrontapp.service.implementation.NoteServiceImpl;
import com.medilabo.medilabofrontapp.service.implementation.PatientServiceImpl;

import feign.FeignException;

@SpringBootTest
class NoteControllerTest {

	@InjectMocks
	private NoteController noteController;

	@Mock
	private PatientServiceImpl patientService;

	@Mock
	private NoteServiceImpl noteService;

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
	void viewNote_Ok_Test() {
		
		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getReturnUrl()).thenReturn(HTMLPage.VIEW_NOTE);

		// WHEN
		String result = noteController.viewNote("111111", model);

		// THEN
		assertEquals(HTMLPage.VIEW_NOTE, result);
	}
	
	@Test
	void viewNote_NotLogged_Test() {
		
		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getReturnUrl()).thenReturn(Redirect.HOME);

		// WHEN
		String result = noteController.viewNote("111111", model);

		// THEN
		assertEquals(Redirect.HOME, result);
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
		assertEquals(Redirect.HOME, result);
	}
	
	@Test
	void validateNote_Ok_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getPatientId()).thenReturn(1);
		when(noteService.addNote(any(String.class), any(NoteBean.class))).thenReturn(note1);
		when(context.getReturnUrl()).thenReturn(Redirect.VIEW_PATIENT +"/1");

		// WHEN
		String result = noteController.validateNote(note1, bResult, model);

		// THEN
		assertEquals(Redirect.VIEW_PATIENT +"/1", result);
	}
	
	@Test
	void validateNote_ResultHasError_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(true);

		// WHEN
		String result = noteController.validateNote(note1, bResult, model);

		// THEN
		assertEquals(HTMLPage.ADD_NOTE, result);
	}
	
	@Test
	void validateNote_Forbidden_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);
		when(context.getPatientId()).thenReturn(1);
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getReturnUrl()).thenReturn(Redirect.HOME);

		// WHEN
		String result = noteController.validateNote(note1, bResult, model);

		// THEN
		assertEquals(Redirect.HOME, result);
	}
	
	@Test
	void updateNoteForm_Ok_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);
		when(context.setAuthHeader()).thenReturn(header);
		when(noteService.getNote(any(String.class), any(String.class))).thenReturn(note1);

		// WHEN
		String result = noteController.updateNoteForm("111111", model);

		// THEN
		assertEquals(HTMLPage.UPDATE_NOTE, result);
	}
	
	@Test
	void updateNoteForm_Forbidden_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);

		// WHEN
		String result = noteController.updateNoteForm("111111", model);

		// THEN
		assertEquals(Redirect.HOME, result);
	}
	
	@Test
	void updateNote_Ok_Test() {
		
		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getPatientId()).thenReturn(1);
		when(noteService.updateNote(any(String.class), any(NoteBean.class))).thenReturn(note1);
		when(context.getReturnUrl()).thenReturn(Redirect.VIEW_PATIENT +"/1");

		// WHEN
		String result = noteController.updateNote("111111", note1, bResult, model);

		// THEN
		assertEquals(Redirect.VIEW_PATIENT +"/1", result);
	}
	
	@Test
	void updateNote_ResultHasError_Test() {
		
		// GIVEN
		when(bResult.hasErrors()).thenReturn(true);

		// WHEN
		String result = noteController.updateNote("111111", note1, bResult, model);

		// THEN
		assertEquals(HTMLPage.UPDATE_NOTE, result);
	}
	
	@Test
	void updateNote_Forbidden_Test() {
		
		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getPatientId()).thenReturn(1);
		when(context.getReturnUrl()).thenReturn(Redirect.VIEW_PATIENT + "/1");

		// WHEN
		String result = noteController.updateNote("111111", note1, bResult, model);

		// THEN
		assertEquals(Redirect.VIEW_PATIENT + "/1", result);
	}
	
	@Test
	void deleteNote_Ok_Test() {
		
		// GIVEN 
		when(context.setAuthHeader()).thenReturn(header);
		when(noteMock.getPatientId()).thenReturn(2);
		when(context.getPatientId()).thenReturn(2);
		when(context.getReturnUrl()).thenReturn(Redirect.VIEW_PATIENT + "/2");

		// WHEN
		String result = noteController.deleteNote("111111", note1, bResult, model);

		// THEN
		assertEquals(Redirect.VIEW_PATIENT + "/2", result);
	}
	
	@Test
	void deleteNote_Forbidden_Test() {
		
		// GIVEN 
		when(context.setAuthHeader()).thenReturn(header);
		when(noteMock.getPatientId()).thenReturn(2);
		when(context.getPatientId()).thenReturn(2);
		when(context.getReturnUrl()).thenReturn(Redirect.HOME);

		// WHEN
		String result = noteController.deleteNote("111111", note1, bResult, model);

		// THEN
		assertEquals(Redirect.HOME, result);
	}
	
}
