package com.medilabo.medilabopatientapp.service;

import java.util.List;

import com.medilabo.medilabopatientapp.entity.Patient;
import com.medilabo.medilabopatientapp.exception.PatientAlreadyExistsException;
import com.medilabo.medilabopatientapp.exception.PatientNotFoundException;

public interface PatientService {

	List<Patient> findAll();

	Patient findById(Integer id) throws PatientNotFoundException;

	Patient save(Patient patient) throws PatientAlreadyExistsException;

	Patient update(Patient patient) throws PatientNotFoundException, PatientAlreadyExistsException;

	void delete(Patient patient) throws PatientNotFoundException;
	
}
