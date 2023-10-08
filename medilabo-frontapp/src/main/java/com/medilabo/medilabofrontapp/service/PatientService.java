package com.medilabo.medilabofrontapp.service;

import java.util.List;

import com.medilabo.medilabofrontapp.bean.PatientBean;

public interface PatientService {

	/**
	 * Returns the list of all patients.
	 * 
	 * @param header corresponding to authorization header
	 * @return List<PatientBean>
	 */
	List<PatientBean> getPatients(String header);

	/**
	 * Returns a patient, given its id.
	 * 
	 * @param header corresponding to authorization header
	 * @param id
	 * @return PatientBean
	 */
	PatientBean getPatient(String header, int id);

	/**
	 * Adds the given patient. 
	 * 
	 * @param header corresponding to authorization header
	 * @param patient
	 * @return PatientBean corresponding to the patient added
	 */
	PatientBean addPatient(String header, PatientBean patient);

	/**
	 * Updates the given patient. 
	 * 
	 * @param header corresponding to authorization header
	 * @param patient
	 * @return PatientBean corresponding to the patient updated
	 */
	PatientBean updatePatient(String header, PatientBean patient);

	/**
	 * Deletes the given patient. 
	 * 
	 * @param header corresponding to authorization header
	 * @param PatientBean corresponding to the patient to delete
	 */
	void deletePatient(String header, PatientBean patient);

}
