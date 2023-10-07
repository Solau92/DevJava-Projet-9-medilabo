package com.medilabo.medilabofrontapp.service;

import java.util.List;

import com.medilabo.medilabofrontapp.bean.PatientBean;

public interface PatientService {

	List<PatientBean> getPatients(String header);

	PatientBean getPatient(String header, int id);

	PatientBean addPatient(String header, PatientBean patient);

	PatientBean updatePatient(String header, PatientBean patient);

	void deletePatient(String header, PatientBean patient);

}
