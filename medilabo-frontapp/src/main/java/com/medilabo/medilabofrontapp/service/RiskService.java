package com.medilabo.medilabofrontapp.service;

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;

public interface RiskService {
	
	/**
	 * Returns the diabetes risk of a patient, given his id. 
	 * 
	 * @param header
	 * @param patientId
	 * @return DiabetesRiskBean
	 */
	DiabetesRiskBean getDiabetesRisk(String header, int patientId);

}
