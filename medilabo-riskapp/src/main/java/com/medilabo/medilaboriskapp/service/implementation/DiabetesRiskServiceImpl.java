package com.medilabo.medilaboriskapp.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;
import com.medilabo.medilaboriskapp.model.RiskLevel;
import com.medilabo.medilaboriskapp.service.DiabetesRiskService;

@Service
public class DiabetesRiskServiceImpl implements DiabetesRiskService {

	@Override
	public DiabetesRisk calculateRisk(PatientBean patient, List<NoteBean> notes) {
		DiabetesRisk risk = new DiabetesRisk();
		risk.setPatientId(1);
		risk.setRisk(RiskLevel.NONE);
		return risk;
	}
	
	private int numberOfTriggers(List<NoteBean> notes) {
		
		
		return 0;
	}

}
