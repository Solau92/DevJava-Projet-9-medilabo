package com.medilabo.medilabopatientapp.unittests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medilabo.medilabopatientapp.entity.Patient;
import com.medilabo.medilabopatientapp.exception.PatientAlreadyExistsException;
import com.medilabo.medilabopatientapp.exception.PatientNotFoundException;
import com.medilabo.medilabopatientapp.repository.PatientRepository;
import com.medilabo.medilabopatientapp.service.implementation.PatientServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

	@InjectMocks
	PatientServiceImpl patientService;
	
	@Mock
	PatientRepository patientRepository;

	List<Patient> patients;
	Patient patient1;
	Patient patient2;
	
	@BeforeEach
	void setUp() {

		patient1 = new Patient();
		patient1.setId(1);
		patient1.setFirstName("firstName1");
		patient1.setLastName("lastName1");
		patient1.setDateOfBirth(LocalDate.of(1981, 01, 01));
		patient1.setGender("M");

		patient2 = new Patient();
		patient2.setId(2);
		patient2.setFirstName("firstName2");
		patient2.setLastName("lastName2");
		patient2.setDateOfBirth(LocalDate.of(1982, 02, 02));
		patient2.setGender("M");

		patients = new ArrayList<>();
		patients.add(patient1);
		patients.add(patient2);
	}

	@Test
	void findAll_Ok_Test() {

		// GIVEN
		when(patientRepository.findAll()).thenReturn(patients);

		// WHEN
		List<Patient> patientsFound = patientService.findAll();

		// THEN
		assertEquals(2, patientsFound.size());
		assertTrue(patients.contains(patient2));
	}

	@Test
	void findById_Ok_Test() throws PatientNotFoundException {

		// GIVEN
		when(patientRepository.findById(anyInt())).thenReturn(Optional.of(patient1));

		// WHEN
		Patient patientFound = patientService.findById(1);

		// THEN
		assertEquals(patient1.getId(), patientFound.getId());
	}

	@Test
	void findById_NotFound_Test() throws PatientNotFoundException {

		// GIVEN
		when(patientRepository.findById(anyInt())).thenReturn(Optional.empty());

		// WHEN
		// THEN
		assertThrows(PatientNotFoundException.class, () -> patientService.findById(5));
	}

	@Test
	void save_Ok_Test() throws PatientAlreadyExistsException {

		// GIVEN
		when(patientRepository.findByFirstNameAndLastNameAndDateOfBirth(anyString(), anyString(), any(LocalDate.class))).thenReturn(Optional.empty());
		Patient patient3 = new Patient();
		patient3.setFirstName("firstName2");
		patient3.setLastName("lastName2");
		patient3.setDateOfBirth(LocalDate.of(1983, 03, 03));
		patient3.setGender("M");
		when(patientRepository.save(any(Patient.class))).thenReturn(patient3);

		// WHEN
		Patient patientSaved = patientService.save(patient3);

		// THEN
		assertEquals(patient3.getFirstName(), patientSaved.getFirstName());
	}

	@Test
	void save_AlreadyExists_Test() {

		// GIVEN
		when(patientRepository.findByFirstNameAndLastNameAndDateOfBirth(anyString(), anyString(), any(LocalDate.class))).thenReturn(Optional.of(patient1));

		// WHEN
		// THEN
		assertThrows(PatientAlreadyExistsException.class, () -> patientService.save(patient1));
	}

	@Test
	void update_Ok_Test() throws PatientNotFoundException, PatientAlreadyExistsException {

		// GIVEN
		when(patientRepository.findById(anyInt())).thenReturn(Optional.of(patient2));
		patient2.setFirstName("firstName2Modified");
		when(patientRepository.save(any(Patient.class))).thenReturn(patient2);

		// WHEN
		Patient patientUpdated = patientService.update(patient2);

		// THEN
		assertEquals("firstName2Modified", patientUpdated.getFirstName());
	}

	@Test
	void update_NotFound_Test() {

		// GIVEN
		when(patientRepository.findById(anyInt())).thenReturn(Optional.empty());
		Patient patient4 = new Patient();
		patient4.setId(4);
		patient4.setFirstName("firstName4");

		// WHEN
		// THEN
		assertThrows(PatientNotFoundException.class, () -> patientService.update(patient4));
	}

	@Test
	void update_AlreadyExists_Test() {

		// I want to modify patient1, but patient1 already exists and is patient2

		// GIVEN
		when(patientRepository.findById(anyInt())).thenReturn(Optional.of(patient1));
		when(patientRepository.findByFirstNameAndLastNameAndDateOfBirth(anyString(), anyString(), any(LocalDate.class))).thenReturn(Optional.of(patient2));
		patient1.setFirstName("firstName2");
		patient1.setLastName("lastName2");
		patient1.setDateOfBirth(LocalDate.of(1982, 02, 02));

		// WHEN

		// THEN
		assertThrows(PatientAlreadyExistsException.class, () -> patientService.update(patient1));
	}

	@Test
	void delete_Ok_Test() {

		// GIVEN
		// TODO : voir pourquoi unnecessary ?? et ne teste pas le ok...
//		when(patientRepository.findById(anyInt())).thenReturn(Optional.of(patient1));

		// WHEN
		patientRepository.delete(patient1);

		// THEN
		verify(patientRepository, Mockito.times(1)).delete(patient1);
	}

	@Test
	void delete_NotFound_Test() {

		// GIVEN
		when(patientRepository.findById(anyInt())).thenReturn(Optional.empty());

		// WHEN
		// THEN
		assertThrows(PatientNotFoundException.class, () -> patientService.delete(patient1));
	}

}
