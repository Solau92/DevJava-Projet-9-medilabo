package com.medilabo.medilabofrontapp.unittests.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.controller.PatientController;
import com.medilabo.medilabofrontapp.model.User;
import com.medilabo.medilabofrontapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilabofrontapp.service.NoteService;
import com.medilabo.medilabofrontapp.service.PatientService;
import com.medilabo.medilabofrontapp.service.implementation.NoteServiceImpl;
import com.medilabo.medilabofrontapp.service.implementation.PatientServiceImpl;
import com.medilabo.medilabofrontapp.service.implementation.RiskServiceImpl;

import feign.FeignException;
import feign.FeignException.Unauthorized;
import feign.Request;

@SpringBootTest
class PatientControllerTest {

	@InjectMocks
	private PatientController patientController;

	@Mock
	private PatientServiceImpl patientService;

	@Mock
	private NoteServiceImpl noteService;

	@Mock
	private RiskServiceImpl riskService;
	
	@Mock
	private Context context;

	@Mock
	private Model model;

	@Mock
	private BindingResult bResult;

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
	void patientList_Ok_Test() {

		// GIVEN
		when(context.setAuthHeader()).thenReturn(header);
		when(patientService.getPatients(any(String.class))).thenReturn(patients);
		when(context.getReturnUrl()).thenReturn(HTMLPage.PATIENTS);

		// WHEN
		String result = patientController.patientList(model);

		// THEN
		assertEquals(HTMLPage.PATIENTS, result);
	}

	@Test
	void patientList_Forbidden_Test() {

		// GIVEN
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getReturnUrl()).thenReturn(HTMLPage.HOME);

		// WHEN
		String result = patientController.patientList(model);

		// THEN
		assertEquals(HTMLPage.HOME, result);
	}

	@Test
	void viewPatient_Ok_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);
		when(context.setAuthHeader()).thenReturn(header);
		when(patientService.getPatient(any(String.class), anyInt())).thenReturn(patient1);
		when(noteService.getNotes(any(String.class), anyInt())).thenReturn(notes);
		when(riskService.getDiabetesRisk(any(String.class), anyInt())).thenReturn(null);
		when(context.getReturnUrl()).thenReturn(HTMLPage.PATIENTS);

		// WHEN
		String result = patientController.viewPatient(1, model);

		// THEN
		assertEquals(HTMLPage.PATIENTS, result);
	}

	@Test
	void viewPatient_NotLogged_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);
		when(context.setAuthHeader()).thenReturn(header);
		when(patientService.getPatient(any(String.class), anyInt())).thenReturn(patient1);
		when(noteService.getNotes(any(String.class), anyInt())).thenReturn(notes);

		// WHEN
		String result = patientController.viewPatient(1, model);

		// THEN
		assertEquals(Redirect.HOME, result);

	}

	@Test
	void addPatientForm_Ok_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);

		// WHEN
		String result = patientController.addPatientForm(model);

		// THEN
		assertEquals(HTMLPage.ADD_PATIENT, result);

	}

	@Test
	void addPatientForm_NotLogged_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);

		// WHEN
		String result = patientController.addPatientForm(model);

		// THEN
		assertEquals(Redirect.HOME, result);
	}

	@Test
	void addPatientValidate_Ok_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(header);
		when(patientService.addPatient(any(String.class), any(PatientBean.class))).thenReturn(patient1);
		when(context.getReturnUrl()).thenReturn(Redirect.PATIENTS);

		// WHEN
		String result = patientController.validate(patient1, bResult, model);

		// THEN
		assertEquals(Redirect.PATIENTS, result);
	}
	
	@Test
	void addPatientValidate_ResultHasError_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(true);

		// WHEN
		String result = patientController.validate(patient1, bResult, model);

		// THEN
		assertEquals(HTMLPage.ADD_PATIENT, result);
	}

	@Test
	void addPatientValidate_Forbidden_Test() {
		
		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getReturnUrl()).thenReturn(Redirect.HOME);

		// WHEN
		String result = patientController.validate(patient1, bResult, model);
	
		// THEN
		assertEquals(Redirect.HOME, result);
	}

	@Test
	void addPatientValidate_PatientAlreadyExists_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getReturnUrl()).thenReturn(HTMLPage.ADD_PATIENT);

		// WHEN
		String result = patientController.validate(patient1, bResult, model);

		// THEN
		assertEquals(HTMLPage.ADD_PATIENT, result);
	}

	@Test
	void updatePatientForm_Ok_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(loggedUser);
		when(patientService.getPatient(any(String.class), anyInt())).thenReturn(patient1);

		// WHEN
		String result = patientController.updatePatientForm(1, model);

		// THEN
		assertEquals(HTMLPage.UPDATE_PATIENT, result);
	}

	@Test
	void updatePatientForm_NotLogged_Test() {

		// GIVEN
		when(context.getLoggedUser()).thenReturn(notLoggedUser);

		// WHEN
		String result = patientController.addPatientForm(model);

		// THEN
		assertEquals(Redirect.HOME, result);
	}

	@Test
	void updatePatientValidate_Ok_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(header);
		when(patientService.updatePatient(any(String.class), any(PatientBean.class))).thenReturn(patient1);
		when(context.getReturnUrl()).thenReturn(HTMLPage.PATIENTS);


		// WHEN
		String result = patientController.updatePatient(1, patient1, bResult, model);

		// THEN
		assertEquals(HTMLPage.PATIENTS, result);
	}
	
	@Test
	void updatePatientValidate_ResultHasError_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(true);

		// WHEN
		String result = patientController.updatePatient(1, patient1, bResult, model);

		// THEN
		assertEquals(HTMLPage.UPDATE_PATIENT, result);
	}

	@Test
	void updatePatientValidate_Forbidden_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getReturnUrl()).thenReturn(HTMLPage.UPDATE_PATIENT);

		// WHEN
		String result = patientController.updatePatient(1, patient1, bResult, model);

		// THEN
		assertEquals(HTMLPage.UPDATE_PATIENT, result);
	}

	@Test
	void updatePatientValidate_PatientAlreadyExists_Test() {

		// GIVEN
		when(bResult.hasErrors()).thenReturn(false);
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getReturnUrl()).thenReturn(HTMLPage.UPDATE_PATIENT);

		// WHEN
		String result = patientController.updatePatient(1, patient1, bResult, model);

		// THEN
		assertEquals(HTMLPage.UPDATE_PATIENT, result);

	}

	@Test
	void deletePatient_Ok_Test() {

		// GIVEN
		when(context.setAuthHeader()).thenReturn(header);
		when(context.getReturnUrl()).thenReturn(Redirect.PATIENTS);

		// WHEN
		String result = patientController.deletePatient(1, patient1, bResult, model);

		// THEN
		assertEquals(Redirect.PATIENTS, result);
	}

	@Test
	void deletePatient_Forbidden_Test() {

		// GIVEN
		when(context.setAuthHeader()).thenReturn(wrongHeader);
		when(context.getReturnUrl()).thenReturn(Redirect.HOME);

		// WHEN
		String result = patientController.deletePatient(1, patient1, bResult, model);

		// THEN
		assertEquals(Redirect.HOME, result);
	}

}
