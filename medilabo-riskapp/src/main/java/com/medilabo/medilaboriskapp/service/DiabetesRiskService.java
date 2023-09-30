package com.medilabo.medilaboriskapp.service;

import java.util.List;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;

public interface DiabetesRiskService {

	DiabetesRisk calculateRisk(PatientBean patient, List<NoteBean> notes);
}
