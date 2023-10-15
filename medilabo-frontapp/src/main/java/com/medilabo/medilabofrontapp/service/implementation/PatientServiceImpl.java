package com.medilabo.medilabofrontapp.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.constants.Redirect;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilabofrontapp.service.PatientService;

import feign.FeignException;

@Service
public class PatientServiceImpl implements PatientService {

	private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

	private MicroservicePatientProxy patientProxy;

	private static Context context;

	public PatientServiceImpl(MicroservicePatientProxy patientProxy, Context context) {
		this.patientProxy = patientProxy;
		this.context = context;
	}

	/**
	 * Returns the list of all patients from the patient app and database.
	 * 
	 * @param header corresponding to authorization header
	 * @return List<PatientBean>
	 */
	@Override
	public List<PatientBean> getPatients(String header) {

		List<PatientBean> patients = new ArrayList<>();

		try {
			patients = patientProxy.patients(header);
			log.debug(patients.toString());
			context.resetUrl();
			context.resetPatientId();
			context.setReturnUrl(HTMLPage.PATIENTS);

		} catch (FeignException e) {
			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				log.info("Exception 401", e.getMessage());
				context.setRedirectAfterExceptionUrl(HTMLPage.PATIENTS);
				context.setReturnUrl(Redirect.HOME);
			}
		}
		return patients;
	}

	/**
	 * Returns a patient from the patient app and database, given its id.
	 * 
	 * @param header corresponding to authorization header
	 * @param id
	 * @return PatientBean
	 */
	@Override
	public PatientBean getPatient(String header, int id) {

		PatientBean patient = new PatientBean();

		try {
			context.setPatientId(id);
			patient = patientProxy.getPatient(header, id);
			log.info("Patient found id : {}, lastname : {}", patient.getId(), patient.getLastName());
			context.setReturnUrl(HTMLPage.VIEW_PATIENT);

		} catch (FeignException e) {
			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());
			context.setMessage("Patient with id {} not found" + id);
			context.setReturnUrl(HTMLPage.PATIENTS);
		}
		return patient;
	}

	/**
	 * Adds the given patient in the patient app and database.
	 * 
	 * @param header  corresponding to authorization header
	 * @param patient
	 * @return PatientBean corresponding to the patient added
	 */
	@Override
	public PatientBean addPatient(String header, PatientBean patient) {

		PatientBean patientSaved = new PatientBean();

		try {
			patientSaved = patientProxy.addPatient(header, patient);
			log.info("Patient saved id : {}, lastname : {}", patientSaved.getId(), patientSaved.getLastName());
			context.resetUrl();
			context.setReturnUrl(Redirect.PATIENTS);

		} catch (FeignException e) {

			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				log.info("Exception 401", e.getMessage());
				context.setRedirectAfterExceptionUrl(HTMLPage.ADD_PATIENT);
				context.setReturnUrl(Redirect.HOME);
			}

			if (e.status() == 400) {
				log.info("A patient with the same firstName, lastName and dateOfBirth already exists", e.getMessage());
				context.setMessage("A patient with the same firstName, lastName and dateOfBirth already exists");
				context.setReturnUrl(HTMLPage.ADD_PATIENT);
			}
		}
		return patientSaved;
	}

	/**
	 * Updates the given patient in the patient app and database.
	 * 
	 * @param header  corresponding to authorization header
	 * @param patient
	 * @return PatientBean corresponding to the patient updated
	 */
	@Override
	public PatientBean updatePatient(String header, PatientBean patient) {

		PatientBean patientUpdated = new PatientBean();

		try {
			patientUpdated = patientProxy.updatePatient(header, patient);
			log.info("Patient saved id : {}, lastname : {}", patientUpdated.getId(), patientUpdated.getLastName());
			context.resetUrl();
			context.setReturnUrl(Redirect.PATIENTS);

		} catch (FeignException e) {

			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				log.info("Exception 401", e.getMessage());
				context.setRedirectAfterExceptionUrl("/patient/validateUpdate/" + context.getPatientId());
				context.setMessage(e.getLocalizedMessage());
				context.setReturnUrl(HTMLPage.UPDATE_PATIENT);
			}

			if (e.status() == 400) {
				log.info("A patient with the same firstName, lastName and dateOfBirth already exists", e.getMessage());
				context.setMessage("A patient with the same firstName, lastName and dateOfBirth already exists");
				context.setReturnUrl(HTMLPage.UPDATE_PATIENT);
			}
		}
		return patientUpdated;
	}

	/**
	 * Deletes the given patient in the patient app and database.
	 * 
	 * @param header      corresponding to authorization header
	 * @param PatientBean corresponding to the patient to delete
	 */
	@Override
	public void deletePatient(String header, PatientBean patient) {

		try {
			patientProxy.deletePatient(header, patient);
			log.info("Patient deleted id : {}, lastname : {}", patient.getId(), patient.getLastName());
			context.resetUrl();
			context.setReturnUrl(Redirect.PATIENTS);

		} catch (FeignException e) {
			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				log.info("Exception 401", e.getMessage());
				context.setRedirectAfterExceptionUrl(HTMLPage.PATIENTS);
				context.setReturnUrl(Redirect.HOME);
			}
		}
	}

}
