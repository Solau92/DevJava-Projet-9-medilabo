package com.medilabo.medilabopatientapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.medilabo.medilabopatientapp.entity.Patient;
import com.medilabo.medilabopatientapp.exception.PatientAlreadyExistsException;
import com.medilabo.medilabopatientapp.exception.PatientNotFoundException;
import com.medilabo.medilabopatientapp.service.PatientService;

@Controller
public class PatientController {

	private PatientService patientService;

	private static final Logger log = LoggerFactory.getLogger(PatientController.class);

	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}

	/**
	 * Gets the list of all the patients.
	 * 
	 * @return ResponseEntity<List<Patient>> with http status ACCEPTED
	 */
	@GetMapping("/patient/patients")
	public ResponseEntity<List<Patient>> findAllPatients() {
		log.info("/patients : Getting the list of all patients");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(patientService.findAll());
	}

	/**
	 * Adds the given patient.
	 * 
	 * @param patient
	 * @return ResponseEntity<Patient> with http status CREATED
	 * @throws PatientAlreadyExistsException if if a patient already exists with the
	 *                                       same firstName, lastName and
	 *                                       dateOfBirth
	 */
	@PostMapping("/patient/validate")
	public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) throws PatientAlreadyExistsException {
		log.info("/patient/validate : Adding patient named {}{}", patient.getFirstName(), patient.getLastName());
		return ResponseEntity.status(HttpStatus.CREATED).body(patientService.save(patient));
	}

	/**
	 * Updates and returns given the patient.
	 * 
	 * @param patient
	 * @return ResponseEntity<Patient> with http status ACCEPTED
	 * @throws PatientNotFoundException if no patient found with the patient's
	 *                                       id
	 * @throws PatientAlreadyExistsException if a patient already exists with the
	 *                                       same firstName, lastName and
	 *                                       dateOfBirth
	 */
	@PostMapping("/patient/validateUpdate")
	public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) throws PatientNotFoundException, PatientAlreadyExistsException {
		log.info("/patient/validateUpdate : Updating patient with id {}", patient.getId());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(patientService.update(patient));
	}

	/**
	 * Deletes the given patient.
	 * 
	 * @param patient
	 * @return ResponseEntity<Patient> with http status ACCEPTED
	 * @throws PatientNotFoundException if the patient was not found 
	 */
	@DeleteMapping("/patient/delete")
	public ResponseEntity<Void> deletePatient(@RequestBody Patient patient) throws PatientNotFoundException {
		log.info("/patient/delete : Deleting patient named {}{}", patient.getFirstName(), patient.getLastName());
		patientService.delete(patient);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * Returns the patient found given its id.
	 * 
	 * @param id
	 * @return ResponseEntity<Patient> with http status ACCEPTED
	 * @throws PatientNotFoundException if the patient was not found 
	 */
	@GetMapping("/patient/{id}")
	public ResponseEntity<Patient> getPatient(@PathVariable int id) throws PatientNotFoundException {
		log.info("/patient/{id} : Geting patient with id {}", id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(patientService.findById(id));
	}

}
