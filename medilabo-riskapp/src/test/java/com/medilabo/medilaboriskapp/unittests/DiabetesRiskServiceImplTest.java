package com.medilabo.medilaboriskapp.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;
import com.medilabo.medilaboriskapp.model.RiskLevel;
import com.medilabo.medilaboriskapp.service.implementation.DiabetesRiskServiceImpl;

@ExtendWith(MockitoExtension.class)
class DiabetesRiskServiceImplTest {
	
	@InjectMocks
	DiabetesRiskServiceImpl diabetesRiskService;
	
	@Mock
	PatientBean patientMock;
	
	PatientBean patient;
	
	List<NoteBean> notes;
	
	@BeforeEach
	void setUp() {
		patient = new PatientBean();
		notes = new ArrayList<>();
	}
	
	@Test
	void calculateRisk_agePlus30_0Trigger_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setDateOfBirth(LocalDate.now().minusYears(40));
		NoteBean note1 = new NoteBean();
		note1.setContent("");
		notes.add(note1);
		
//		when(patientMock.getId()).thenReturn(1);
//		when(patientMock.getDateOfBirth()).thenReturn(LocalDate.of(1950, 01, 01));
//		when(patientMock.getGender()).thenReturn("M");
//		when(diabetesRiskService.numberOfTriggers(any(List.class))).thenReturn(0);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.NONE, result.getRisk());	
	}
	
	@Test
	void calculateRisk_agePlus30_3Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setDateOfBirth(LocalDate.now().minusYears(40));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.BORDERLINE, result.getRisk());	
	}
	
	@Test
	void calculateRisk_agePlus30_6Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setDateOfBirth(LocalDate.now().minusYears(40));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeur rechute vertiges");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.IN_DANGER, result.getRisk());	
	}
	
	@Test
	void calculateRisk_agePlus30_8Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setDateOfBirth(LocalDate.now().minusYears(40));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeur rechute vertiges anticorps réaction");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.EARLY_ONSET, result.getRisk());	
	}
	
	@Test
	void calculateRisk_agePlus30_plus8Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setDateOfBirth(LocalDate.now().minusYears(40));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeur rechute vertiges anticorps réaction microalbumine");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.EARLY_ONSET, result.getRisk());	
	}
	
	@Test
	void calculateRisk_ageLess30_H_0Trigger_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("M");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.NONE, result.getRisk());	
	}
	
	@Test
	void calculateRisk_ageLess30_H_3Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("M");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.IN_DANGER, result.getRisk());	
	}
	
	@Test
	void calculateRisk_ageLess30_H_6Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("M");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeur rechute vertiges");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.EARLY_ONSET, result.getRisk());	
	}
	
	@Test
	void calculateRisk_ageLess30_H_9Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("M");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeur rechute vertiges anticorps réaction microalbumine hémoglobine A1C");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.EARLY_ONSET, result.getRisk());	
	}
	
	////////////////
	
	@Test
	void calculateRisk_ageLess30_F_0Trigger_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("F");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.NONE, result.getRisk());	
	}
	
	@Test
	void calculateRisk_ageLess30_F_4Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("F");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeuse");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.IN_DANGER, result.getRisk());	
	}
	
	@Test
	void calculateRisk_ageLess30_F_7Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("F");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeuse rechute vertiges anticorps");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.EARLY_ONSET, result.getRisk());	
	}
	
	@Test
	void calculateRisk_ageLess30_F_9Triggers_Test() {
		
		// GIVEN 
		patient.setId(1);
		patient.setGender("F");
		patient.setDateOfBirth(LocalDate.now().minusYears(20));
		NoteBean note1 = new NoteBean();
		note1.setContent("taille poids anormal fumeuse rechute vertiges anticorps réaction microalbumine hémoglobine A1C");
		notes.add(note1);
		
		// WHEN 
		DiabetesRisk result = diabetesRiskService.calculateRisk(patient, notes);
		
		// THEN 
		assertEquals(1, result.getPatientId());
		assertEquals(RiskLevel.EARLY_ONSET, result.getRisk());	
	}

}
