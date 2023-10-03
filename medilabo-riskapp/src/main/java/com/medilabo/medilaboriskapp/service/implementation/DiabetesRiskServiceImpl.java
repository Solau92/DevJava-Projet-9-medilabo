package com.medilabo.medilaboriskapp.service.implementation;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;
import com.medilabo.medilaboriskapp.model.RiskLevel;
import com.medilabo.medilaboriskapp.model.Triggers;
import com.medilabo.medilaboriskapp.service.DiabetesRiskService;

@Service
public class DiabetesRiskServiceImpl implements DiabetesRiskService {

	private static final Logger log = LoggerFactory.getLogger(DiabetesRiskServiceImpl.class);

	@Override
	public DiabetesRisk calculateRisk(PatientBean patient, List<NoteBean> notes) {

		DiabetesRisk risk = new DiabetesRisk();
		risk.setPatientId(patient.getId());

		int age = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
		int nbOfTriggers = numberOfTriggers(notes);
		String gender = patient.getGender();

		if (age > 30) {

			switch (nbOfTriggers) {

			case 0, 1 -> risk.setRisk(RiskLevel.NONE);
			case 2, 3, 4, 5 -> risk.setRisk(RiskLevel.BORDERLINE);
			case 6, 7 -> risk.setRisk(RiskLevel.IN_DANGER);
			case 8 -> risk.setRisk(RiskLevel.EARLY_ONSET);
			default -> risk.setRisk(RiskLevel.EARLY_ONSET);
			}

		// age < 30
		} else {

			if (gender.equals("M")) {
				switch (nbOfTriggers) {
				case 0, 1, 2 -> risk.setRisk(RiskLevel.NONE);
				case 3, 4 -> risk.setRisk(RiskLevel.IN_DANGER);
				case 5, 6, 7, 8 -> risk.setRisk(RiskLevel.EARLY_ONSET);
				default -> risk.setRisk(RiskLevel.EARLY_ONSET);
				}

			// gender : F
			} else {
				switch (nbOfTriggers) {
				case 0, 1, 2, 3 -> risk.setRisk(RiskLevel.NONE);
				case 4, 5, 6 -> risk.setRisk(RiskLevel.IN_DANGER);
				case 7, 8 -> risk.setRisk(RiskLevel.EARLY_ONSET);
				default -> risk.setRisk(RiskLevel.EARLY_ONSET);
				}
			}
		}
		return risk;
	}

	private int numberOfTriggers(List<NoteBean> notes) {

		int nbOfTriggers = 0;

		for (NoteBean n : notes) {

			for (Triggers t : Triggers.values()) {
				if (n.getContent().toLowerCase().contains(t.label.toLowerCase())) {
					nbOfTriggers++;
					log.info(t.label);
				}
			}
		}
		log.info("Number of triggers : " + nbOfTriggers);
		return nbOfTriggers;
	}

}
