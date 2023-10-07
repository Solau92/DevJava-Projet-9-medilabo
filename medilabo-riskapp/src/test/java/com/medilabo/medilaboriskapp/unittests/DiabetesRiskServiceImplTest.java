package com.medilabo.medilaboriskapp.unittests;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;
import com.medilabo.medilaboriskapp.model.RiskLevel;
import com.medilabo.medilaboriskapp.service.implementation.DiabetesRiskServiceImpl;

@SpringBootTest
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
	void calculateRisk_AgePlus30_0Trigger_Test() {
		
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

}
