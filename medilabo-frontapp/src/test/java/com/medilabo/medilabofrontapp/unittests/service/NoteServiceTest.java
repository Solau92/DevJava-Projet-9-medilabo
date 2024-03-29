package com.medilabo.medilabofrontapp.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.service.implementation.NoteServiceImpl;
import com.medilabo.medilabofrontapp.service.implementation.PatientServiceImpl;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

	@InjectMocks
	NoteServiceImpl noteService;

	@Mock
	MicroserviceNoteProxy noteProxy;

	@Mock
	Context context;
	
	LogCaptor logCaptor = LogCaptor.forClass(NoteServiceImpl.class);

	PatientBean patient1;
	String header;
	String wrongHeader;
	User loggedUser;
	User notLoggedUser;
	List<NoteBean> notes;
	NoteBean note1;
	NoteBean note2;
	FeignException unauthorizedException;
	
	@BeforeEach
	void setUp() {

		header = "ok";
		wrongHeader = "notOk";

		loggedUser = new User();
		loggedUser.setUsername("username");
		loggedUser.setPassword("password");

		notLoggedUser = new User();
		notLoggedUser.setUsername(null);
		notLoggedUser.setPassword("password");

		patient1 = new PatientBean();
		patient1.setId(1);
		patient1.setFirstName("firstName1");
		patient1.setLastName("lastName1");
		patient1.setDateOfBirth(LocalDate.of(1981, 01, 01));
		patient1.setGender("M");
		
		note1 = new NoteBean();
		note1.setId("111111");
		note1.setDate(LocalDate.now());
		note1.setContent("content1");
		note1.setPatientId(1);
		
		note2 = new NoteBean();
		note2.setId("222222");
		note2.setDate(LocalDate.now());
		note2.setContent("content2");
		note2.setPatientId(2);

		notes = new ArrayList<>();
		notes.add(note1);
		notes.add(note2);
		
		unauthorizedException = new FeignException.Unauthorized("", Request.create(HttpMethod.GET, "", new HashMap(), new byte[0], Charset.defaultCharset()), new byte[0], new HashMap<>());

	}
	
	@Test
	void addNote_Ok_Test() {

		// GIVEN
		when(context.getPatientId()).thenReturn(1);
		when(noteProxy.addNote(any(String.class), any(NoteBean.class))).thenReturn(note1);

		// WHEN
		NoteBean result = noteService.addNote(header, note1);

		// THEN
		assertEquals(note1.getPatientId(), result.getPatientId());
	}
	
	@Test
	void addNote_Forbidden_Test() {

		// GIVEN
		when(context.getPatientId()).thenReturn(1);
		when(noteProxy.addNote(any(String.class), any(NoteBean.class))).thenThrow(unauthorizedException);

		// WHEN
		noteService.addNote(header, note1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("FeignException status :"));
	}
	
	@Test
	void getNote_Ok_Test() {
		
		// GIVEN
		when(noteProxy.getNote(any(String.class), any(String.class))).thenReturn(note1);

		// WHEN
		NoteBean result = noteService.getNote(header, note1.getId());

		// THEN
		assertEquals(note1.getPatientId(), result.getPatientId());
	}
	
	@Test
	void getNote_Forbidden_Test() {
		
		// GIVEN
		when(noteProxy.getNote(any(String.class), any(String.class))).thenThrow(unauthorizedException);

		// WHEN
		noteService.getNote(header, note1.getId());

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}
	
	@Test
	void getNotes_Ok_Test() {
		
		// GIVEN
		when(noteProxy.getNotes(any(String.class), any(Integer.class))).thenReturn(notes);

		// WHEN
		List<NoteBean> result = noteService.getNotes(header, patient1.getId());

		// THEN
		assertTrue(result.contains(note2));
	}
	
	@Test
	void getNotes_Forbidden_Test() {
		
		// GIVEN
		when(noteProxy.getNotes(any(String.class), any(Integer.class))).thenThrow(unauthorizedException);

		// WHEN
		noteService.getNotes(header, 1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}
	
	@Test
	void updateNote_Ok_Test() {

		// GIVEN
		when(context.getPatientId()).thenReturn(1);
		when(noteProxy.updateNote(any(String.class), any(NoteBean.class))).thenReturn(note1);

		// WHEN
		NoteBean result = noteService.updateNote(header, note1);

		// THEN
		assertEquals(note1.getPatientId(), result.getPatientId());
	}
	
	@Test
	void updateNote_Forbidden_Test() {

		// GIVEN
		when(context.getPatientId()).thenReturn(1);
		when(noteProxy.updateNote(any(String.class), any(NoteBean.class))).thenThrow(unauthorizedException);

		// WHEN
		noteService.updateNote(header, note1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}
	
	
	@Test
	void deleteNote_Ok_Test() {

		// GIVEN

		// WHEN
		noteService.deleteNote(header, note1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("Note deleted id : " + note1.getId()));
	}
	
	@Test
	void deleteNote_Forbidden_Test() {

		// GIVEN
		doThrow(unauthorizedException).when(noteProxy).deleteNote(any(String.class), any(NoteBean.class));

		// WHEN
		noteService.deleteNote(header, note1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}
	
}
