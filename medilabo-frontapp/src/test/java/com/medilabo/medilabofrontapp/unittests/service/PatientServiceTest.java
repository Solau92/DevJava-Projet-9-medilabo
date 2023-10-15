package com.medilabo.medilabofrontapp.unittests.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import nl.altindag.log.LogCaptor;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilabofrontapp.service.implementation.PatientServiceImpl;
import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

	@InjectMocks
	PatientServiceImpl patientService;

	@Mock
	MicroservicePatientProxy patientProxy;

	@Mock
	Context context;
	
	LogCaptor logCaptor = LogCaptor.forClass(PatientServiceImpl.class);

	List<PatientBean> patients;
	PatientBean patient1;
	PatientBean patient2;
	String header;
	String wrongHeader;
	User loggedUser;
	User notLoggedUser;
	List<NoteBean> notes;
	FeignException unauthorizedException;
	FeignException badRequestException;

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

		patient2 = new PatientBean();
		patient2.setId(2);
		patient2.setFirstName("firstName2");
		patient2.setLastName("lastName2");
		patient2.setDateOfBirth(LocalDate.of(1982, 02, 02));
		patient2.setGender("M");

		patients = new ArrayList<>();
		patients.add(patient1);
		patients.add(patient2);

		notes = new ArrayList<>();
		
		logCaptor.setLogLevelToInfo();
		
		unauthorizedException = new FeignException.Unauthorized("", Request.create(HttpMethod.GET, "", new HashMap(), new byte[0], Charset.defaultCharset()), new byte[0], new HashMap<>());
		badRequestException = new FeignException.BadRequest("", Request.create(HttpMethod.GET, "", new HashMap(), new byte[0], Charset.defaultCharset()), new byte[0], new HashMap<>());

	}

	@Test
	void getPatients_Ok_Test() {

		// GIVEN
		when(patientProxy.patients(any(String.class))).thenReturn(patients);

		// WHEN
		List<PatientBean> result = patientService.getPatients(header);

		// THEN
		assertTrue(result.contains(patient1));
	}

	@Test
	void getPatients_Forbidden_Test() {

		// GIVEN
		when(patientProxy.patients(any(String.class))).thenThrow(unauthorizedException);

		// WHEN
		patientService.getPatients(header);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}

	@Test
	void getPatient_Ok_Test() {

		// GIVEN
		when(patientProxy.getPatient(any(String.class), anyInt())).thenReturn(patient1);

		// WHEN
		PatientBean result = patientService.getPatient(header, 1);

		// THEN
		assertEquals(patient1.getFirstName(), result.getFirstName());
	}

	@Test
	void getPatient_Forbidden_Test() {

		// GIVEN
		when(patientProxy.getPatient(any(String.class), anyInt())).thenThrow(unauthorizedException);

		// WHEN
		patientService.getPatient(wrongHeader, 0);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
	}

	@Test
	void addPatient_Ok_Test() {

		// GIVEN
		when(patientProxy.addPatient(any(String.class), any(PatientBean.class))).thenReturn(patient1);

		// WHEN
		PatientBean result = patientService.addPatient(header, patient1);

		// THEN
		assertEquals(patient1.getFirstName(), result.getFirstName());
	}

	@Test
	void addPatient_Forbidden_Test() {

		// GIVEN
		when(patientProxy.addPatient(any(String.class), any(PatientBean.class))).thenThrow(unauthorizedException);

		// WHEN
		patientService.addPatient(header, patient1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}
	
	@Test
	void addPatient_AlreadyExists_Test() {

		// GIVEN
		when(patientProxy.addPatient(any(String.class), any(PatientBean.class))).thenThrow(badRequestException);

		// WHEN
		patientService.addPatient(header, patient1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("A patient with the same firstName, lastName and dateOfBirth already exists"));
	}

	@Test
	void updatePatient_Ok_Test() {

		// GIVEN
		when(patientProxy.updatePatient(any(String.class), any(PatientBean.class))).thenReturn(patient2);

		// WHEN
		PatientBean result = patientService.updatePatient(header, patient2);

		// THEN
		assertEquals(patient2.getLastName(), result.getLastName());
	}

	@Test
	void updatePatient_Forbidden_Test() {

		// GIVEN
		when(patientProxy.updatePatient(any(String.class), any(PatientBean.class))).thenThrow(unauthorizedException);

		// WHEN
		patientService.updatePatient(wrongHeader, patient2);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}
	
	@Test
	void updatePatient_AlreadyExists_Test() {

		// GIVEN
		when(patientProxy.updatePatient(any(String.class), any(PatientBean.class))).thenThrow(badRequestException);

		// WHEN
		patientService.updatePatient(wrongHeader, patient2);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("A patient with the same firstName, lastName and dateOfBirth already exists"));
	}

	@Test
	void deletePatient_Ok_Test() {

		// GIVEN

		// WHEN
		patientService.deletePatient(header, patient2);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("Patient deleted id : " + patient2.getId()));
	}
	
	@Test
	void deletePatient_Forbidden_Test() {

		// GIVEN
		doThrow(unauthorizedException).when(patientProxy).deletePatient(any(String.class), any(PatientBean.class));

		// WHEN
		patientService.deletePatient(header, patient2);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}

}
