package com.medilabo.medilaboriskapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;
import com.medilabo.medilaboriskapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilaboriskapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilaboriskapp.service.DiabetesRiskService;

@Controller
public class DiabetesRiskController {
	
	private static final Logger log = LoggerFactory.getLogger(DiabetesRiskController.class);
	
	private DiabetesRiskService riskService;
	
	private final MicroservicePatientProxy patientProxy;
	
	private final MicroserviceNoteProxy noteProxy;
	
	public DiabetesRiskController(DiabetesRiskService riskService, MicroservicePatientProxy patientProxy, MicroserviceNoteProxy noteProxy) {
		this.riskService = riskService;
		this.patientProxy = patientProxy;
		this.noteProxy = noteProxy;
	}

	/**
	 * Returns the risk of diabetes for a patient, given his id. 
	 * 
	 * @param patientId
	 * @return ResponseEntity<DiabetesRisk> with http status ACCEPTED
	 */
	@GetMapping("/risk/{patientId}")
	public ResponseEntity<DiabetesRisk> getDiabetesRisk(@PathVariable int patientId) {
		
		log.info("/risk/{}", patientId);
		
		PatientBean patient = patientProxy.getPatient(patientId);
		log.info("Calculating risk for patient : {}", patient);
		
		List<NoteBean> notes = noteProxy.getNotes(patientId);
		log.info("List of notes for patient : {}", notes);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(riskService.calculateRisk(patient, notes));		
	}

}
