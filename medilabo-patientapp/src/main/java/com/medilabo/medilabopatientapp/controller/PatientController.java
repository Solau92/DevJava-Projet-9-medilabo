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

	private Logger log = LoggerFactory.getLogger(PatientController.class);

	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}

	@GetMapping("/patients")
	public ResponseEntity<List<Patient>> findAllPatients() {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(patientService.findAll());
	}

	@PostMapping("/patient/validate")
	public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) throws PatientAlreadyExistsException {

		return ResponseEntity.status(HttpStatus.CREATED).body(patientService.save(patient));
	}

	@PostMapping("/patient/validateUpdate")
	public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) throws PatientNotFoundException, PatientAlreadyExistsException {

		log.info("/patient/update in PatientController");
		return ResponseEntity.status(HttpStatus.CREATED).body(patientService.update(patient));
	}

	@DeleteMapping("/patient/delete")
	public ResponseEntity<Void> deletePatient(@RequestBody Patient patient) throws PatientNotFoundException {

		patientService.delete(patient);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@GetMapping("/patient/{id}")
	public ResponseEntity<Patient> getPatient(@PathVariable int id) throws PatientNotFoundException {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(patientService.findById(id));
	}

}
