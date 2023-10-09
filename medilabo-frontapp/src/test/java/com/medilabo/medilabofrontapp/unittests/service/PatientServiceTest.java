package com.medilabo.medilabofrontapp.unittests.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilabofrontapp.service.implementation.PatientServiceImpl;

import feign.FeignException;

@SpringBootTest
public class PatientServiceTest {

	@InjectMocks
	PatientServiceImpl patientService;

	@Mock
	MicroservicePatientProxy patientProxy;

	@Mock
	Context context;

	List<PatientBean> patients;
	PatientBean patient1;
	PatientBean patient2;
	String header;
	String wrongHeader;
	User loggedUser;
	User notLoggedUser;
	List<NoteBean> notes;

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

	@Disabled
	@Test
	void getPatients_Forbidden_Test() {

		// GIVEN
		when(patientProxy.patients(any(String.class))).thenThrow(FeignException.class);
//		doCallRealMethod().when(context).setRedirectAfterExceptionUrl(any(String.class));
//		context.setRedirectAfterExceptionUrl(HTMLPage.PATIENTS);

//		context.setRedirectAfterExceptionUrl("URL"));
//		doAnswer(
//			invocation -> { ;
//		}).

		// WHEN
		List<PatientBean> result = patientService.getPatients(header);

		// THEN
		assertTrue(result.isEmpty());
//		verify(context, times(1)).setRedirectAfterExceptionUrl(any(String.class));
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
	
	@Disabled
	@Test
	void getPatient_Forbidden_Test() {

//		// GIVEN
//		when(patientProxy.getPatient(any(String.class), anyInt())).thenReturn(patient1);
//
//		// WHEN
//		PatientBean result = patientService.getPatient(header, 1);
//
//		// THEN
//		assertEquals(patient1.getFirstName(), result.getFirstName());
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
	
	@Disabled
	@Test
	void addPatient_Forbidden_Test() {

		// GIVEN
		when(patientProxy.addPatient(any(String.class), any(PatientBean.class))).thenThrow(FeignException.class);
//		doCallRealMethod().when(context).setRedirectAfterExceptionUrl(any(String.class));
		
		// WHEN
		PatientBean result = patientService.addPatient(header, patient1);

		// THEN
//		verify(context, times(1)).setRedirectAfterExceptionUrl(HTMLPage.ADD_PATIENT);
		assertEquals(null, result.getFirstName());
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
	
	@Disabled
	@Test
	void updatePatient_Forbidden_Test() {

//		// GIVEN
//		when(patientProxy.updatePatient(any(String.class), any(PatientBean.class))).thenReturn(patient2);
//
//		// WHEN
//		PatientBean result = patientService.updatePatient(header, patient2);
//
//		// THEN
//		assertEquals(patient2.getLastName(), result.getLastName());
	}
	
	@Disabled
	@Test
	void deletePatient_Ok_Test() {

//		// GIVEN
//		when(patientProxy.deletePatient(any(String.class), any(PatientBean.class)));
//
//		// WHEN
//		patientService.deletePatient(header, patient2);
//
//		// THEN
//		assertEquals(patient2.getLastName(), result.getLastName());
	}

}
