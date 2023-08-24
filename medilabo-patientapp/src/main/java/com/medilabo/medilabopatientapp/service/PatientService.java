package com.medilabo.medilabopatientapp.service;

import java.util.List;

import com.medilabo.medilabopatientapp.entity.Patient;
import com.medilabo.medilabopatientapp.exception.PatientAlreadyExistsException;
import com.medilabo.medilabopatientapp.exception.PatientNotFoundException;

public interface PatientService {

	/**
	 * Returns a list of all the patients.
	 * 
	 * @return List<Patient>
	 */
	List<Patient> findAll();

	/**
	 * Returns the patient found given its id.
	 * 
	 * @param id
	 * @return Patient
	 * @throws PatientNotFoundException if no patient found with the given id
	 */
	Patient findById(Integer id) throws PatientNotFoundException;

	/**
	 * Saves and return the given patient.
	 * 
	 * @param patient
	 * @return Patient
	 * @throws PatientAlreadyExistsException if a patient already exists with the
	 *                                       same firstName, lastName and
	 *                                       dateOfBirth
	 */
	Patient save(Patient patient) throws PatientAlreadyExistsException;

	/**
	 * Updates and returns given the patient.
	 * 
	 * @param patient
	 * @return Patient
	 * @throws PatientNotFoundException      if no patient found with the patient's
	 *                                       id
	 * @throws PatientAlreadyExistsException if a patient already exists with the
	 *                                       same firstName, lastName and
	 *                                       dateOfBirth
	 */
	Patient update(Patient patient) throws PatientNotFoundException, PatientAlreadyExistsException;

	/**
	 * Deletes the given patient.
	 * 
	 * @param patient
	 * @throws PatientNotFoundException if the patient was not found 
	 */
	void delete(Patient patient) throws PatientNotFoundException;

}
