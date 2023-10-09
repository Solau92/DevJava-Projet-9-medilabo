package com.medilabo.medilaboriskapp.service;

import java.util.List;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;

public interface DiabetesRiskService {

	/**
	 * Returns the risk of diabetes for a given patient.
	 * 
	 * @param Patient
	 * @param List<NoteBean> all the notes corresponding to the patient
	 * @return DiabetesRisk
	 */
	DiabetesRisk calculateRisk(PatientBean patient, List<NoteBean> notes);
}
