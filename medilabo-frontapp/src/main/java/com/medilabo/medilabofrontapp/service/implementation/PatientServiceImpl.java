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

	@Override
	public List<PatientBean> getPatients(String header) {

		List<PatientBean> patients = new ArrayList<>();

		try {
			patients = patientProxy.patients(header);
			context.resetUrl();
			context.resetPatientId();
			context.setReturnUrl(HTMLPage.PATIENTS);

		} catch (FeignException e) {
			if (e.status() == 401) {
				context.setRedirectAfterExceptionUrl(HTMLPage.PATIENTS);
				context.setReturnUrl(HTMLPage.HOME);
			}
		}
		return patients;
	}

	@Override
	public PatientBean getPatient(String header, int id) {

		PatientBean patient = new PatientBean();

		try {
			context.setPatientId(id);
			patient = patientProxy.getPatient(header, id);
			context.setReturnUrl(HTMLPage.VIEW_PATIENT);
		} catch (FeignException e) {
			// TODO : Gerer PatientNotFoundException
			log.info("Exception status : {}", e.status());
			context.setMessage("Patient with id {} not found" + id);
			context.setReturnUrl(HTMLPage.PATIENTS);
		}
		return patient;
	}

	@Override
	public PatientBean addPatient(String header, PatientBean patient) {

		PatientBean patientSaved = new PatientBean();

		try {
			patientSaved = patientProxy.addPatient(header, patient);
			context.resetUrl();
			context.setReturnUrl(Redirect.PATIENTS);

		} catch (FeignException e) {

			log.info("feignException status : " + e.status() + " message : " + e.getMessage());

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setRedirectAfterExceptionUrl("/patient/add");
				context.setReturnUrl(Redirect.HOME);
			}

			if (e.status() == 400) {
				log.info("Exception status : {}", e.status());
				context.setMessage("A patient with the same firstName, lastName and dateOfBirth already exists");
				context.setReturnUrl(HTMLPage.ADD_PATIENT);
			}
		}
		return patientSaved;
	}

	@Override
	public PatientBean updatePatient(String header, PatientBean patient) {

		PatientBean patientUpdated = new PatientBean();

		try {
			patientProxy.updatePatient(header, patient);
			context.resetUrl();
			context.setReturnUrl(Redirect.PATIENTS);
		} catch (FeignException e) {

			log.info("statut : {} message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				log.info("Exception status : {}", e.status());
				context.setRedirectAfterExceptionUrl("/patient/validateUpdate/" + context.getPatientId());
				context.setMessage(e.getLocalizedMessage());
				context.setReturnUrl(HTMLPage.UPDATE_PATIENT);
			}

			if (e.status() == 400) {
				log.info("Exception status : {}", e.status());
				context.setMessage("A patient with the same firstName, lastName and dateOfBirth already exists");
				context.setReturnUrl(HTMLPage.UPDATE_PATIENT);
			}
		}
		return patientUpdated;
	}

	@Override
	public void deletePatient(String header, PatientBean patient) {

		try {
			patientProxy.deletePatient(header, patient);
			context.resetUrl();
			context.setReturnUrl(Redirect.PATIENTS);
		} catch (FeignException e) {
			if (e.status() == 401) {
				context.setRedirectAfterExceptionUrl("/patient/patients");
				context.setReturnUrl(Redirect.HOME);
			}
		}
	}

}
