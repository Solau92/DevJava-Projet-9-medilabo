//package com.medilabo.medilabopatientapp.unittests;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import com.medilabo.medilabopatientapp.controller.PatientController;
//import com.medilabo.medilabopatientapp.entity.Patient;
//import com.medilabo.medilabopatientapp.exception.PatientAlreadyExistsException;
//import com.medilabo.medilabopatientapp.exception.PatientNotFoundException;
//import com.medilabo.medilabopatientapp.service.implementation.PatientServiceImpl;
//
//@SpringBootTest
//public class PatientControllerTest {
//
//	@InjectMocks
//	private PatientController patientControler;
//
//	@Mock
//	private PatientServiceImpl patientService;
//
//	List<Patient> patients;
//	Patient patient1;
//	Patient patient2;
//
//	@BeforeEach
//	void setUp() {
//		patient1 = new Patient();
//		patient1.setId(1);
//		patient1.setFirstName("firstName1");
//		patient1.setLastName("lastName1");
//		patient1.setDateOfBirth(LocalDate.of(1981, 01, 01));
//		patient1.setGender("M");
//
//		patient2 = new Patient();
//		patient2.setId(2);
//		patient2.setFirstName("firstName2");
//		patient2.setLastName("lastName2");
//		patient2.setDateOfBirth(LocalDate.of(1982, 02, 02));
//		patient2.setGender("M");
//
//		patients = new ArrayList<>();
//		patients.add(patient1);
//		patients.add(patient2);
//	}
//
//	@Test
//	void findAllPatients_Ok_Test() {
//
//		// GIVEN
//		when(patientService.findAll()).thenReturn(patients);
//
//		// WHEN
//		ResponseEntity<List<Patient>> result = patientControler.findAllPatients();
//
//		// THEN
//		assertEquals(2, result.getBody().size());
//		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
//	}
//
//	@Test
//	void addPatient_Ok_Test() throws PatientAlreadyExistsException {
//
//		// GIVEN
//		when(patientService.save(any(Patient.class))).thenReturn(patient1);
//
//		// WHEN
//		ResponseEntity<Patient> result = patientControler.addPatient(patient1);
//				
//		// THEN
//		assertEquals(patient1, result.getBody());
//		assertEquals(HttpStatus.CREATED, result.getStatusCode());
//	}
//
//	@Test
//	void addPatient_PatientAlreadyExists_Test() throws PatientAlreadyExistsException {
//
//		// GIVEN
//		when(patientService.save(any(Patient.class))).thenThrow(PatientAlreadyExistsException.class);
//		
//		// WHEN
//		// THEN
//		assertThrows(PatientAlreadyExistsException.class, () -> patientControler.addPatient(patient1));
//	}
//
//	@Test
//	void updatePatient_Ok_Test() throws PatientNotFoundException, PatientAlreadyExistsException {
//
//		// GIVEN
//		when(patientService.update(any(Patient.class))).thenReturn(patient1);
//
//		// WHEN
//		ResponseEntity<Patient> result = patientControler.updatePatient(patient1);
//
//		// THEN
//		assertEquals(patient1, result.getBody());
//		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
//	}
//
//	@Test
//	void updatePatient_PatientNotFound_Test() throws PatientNotFoundException, PatientAlreadyExistsException {
//
//		// GIVEN
//		when(patientService.update(any(Patient.class))).thenThrow(PatientNotFoundException.class);
//
//		// WHEN
//		// THEN
//		assertThrows(PatientNotFoundException.class, () -> patientControler.updatePatient(patient1));
//	}
//
//	@Test
//	void updatePatient_PatientAlreadyExists_Test() throws PatientNotFoundException, PatientAlreadyExistsException {
//
//		// GIVEN
//		when(patientService.update(any(Patient.class))).thenThrow(PatientAlreadyExistsException.class);
//
//		// WHEN
//		// THEN
//		assertThrows(PatientAlreadyExistsException.class, () -> patientControler.updatePatient(patient1));
//	}
//
//	@Test
//	void deletePatient_Ok_Test() throws PatientNotFoundException {
//
//		// GIVEN
//		// WHEN
//		ResponseEntity<Void> result = patientControler.deletePatient(patient1);
//				
//		// THEN
//		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
//	}
//
//	@Test
//	void deletePatient_PatientNotFound_Test() throws PatientNotFoundException {
//
//		// GIVEN
//		doThrow(PatientNotFoundException.class).when(patientService).delete(any(Patient.class));
//
//		// WHEN
//		// THEN
//		assertThrows(PatientNotFoundException.class, () -> patientControler.deletePatient(patient1));
//	}
//
//	@Test
//	void getPatient_Ok_Test() throws PatientNotFoundException {
//
//		// GIVEN
//		when(patientService.findById(anyInt())).thenReturn(patient1);
//
//		// WHEN
//		ResponseEntity<Patient> result = patientControler.getPatient(1);
//
//		// THEN
//		assertEquals(patient1, result.getBody());
//		assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
//	}
//
//	@Test
//	void getPatient_PatientNotFound_Test() throws PatientNotFoundException {
//
//		// GIVEN
//		when(patientService.findById(anyInt())).thenThrow(PatientNotFoundException.class);
//		
//		// WHEN
//		// THEN
//		assertThrows(PatientNotFoundException.class, () -> patientControler.getPatient(1));
//
//	}
//
//}
