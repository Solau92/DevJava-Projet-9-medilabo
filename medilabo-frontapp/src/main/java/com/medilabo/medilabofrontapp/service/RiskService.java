package com.medilabo.medilabofrontapp.service;

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;

public interface RiskService {
	
	DiabetesRiskBean getDiabetesRisk(String header, int patientId);

}
