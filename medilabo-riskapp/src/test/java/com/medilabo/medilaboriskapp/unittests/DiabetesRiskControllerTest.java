package com.medilabo.medilaboriskapp.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.controller.DiabetesRiskController;
import com.medilabo.medilaboriskapp.model.DiabetesRisk;
import com.medilabo.medilaboriskapp.model.RiskLevel;
import com.medilabo.medilaboriskapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilaboriskapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilaboriskapp.service.DiabetesRiskService;

@ExtendWith(MockitoExtension.class)
class DiabetesRiskControllerTest {
	
	@InjectMocks
	DiabetesRiskController diabetesController;
	
	@Mock
	DiabetesRiskService riskService;
	
	@Mock
	MicroservicePatientProxy patientProxy;
	
	@Mock
	MicroserviceNoteProxy noteProxy;
	
	PatientBean patient1;
	NoteBean note1;
	NoteBean note2;
	List<NoteBean> notes;
	DiabetesRisk diabetesRisk1;

	@BeforeEach
	void setUp() {
		
		patient1 = new PatientBean();
		patient1.setId(1);
		patient1.setFirstName("firstName1");
		patient1.setLastName("lastName1");
		patient1.setDateOfBirth(LocalDate.now().minusYears(40));
		patient1.setGender("M");
		
		note1 = new NoteBean();
		note1.setId("111111");
		note1.setDate(LocalDate.now());
		note1.setContent("content 1");
		note1.setPatientId(1);

		note2 = new NoteBean();
		note2.setId("222222");
		note2.setDate(LocalDate.now());
		note2.setContent("content 2");
		note2.setPatientId(1);

		notes = new ArrayList<>();
		notes.add(note1);
		notes.add(note2);
		
		diabetesRisk1 = new DiabetesRisk();
		diabetesRisk1.setPatientId(1);
		diabetesRisk1.setRisk(RiskLevel.NONE);
	}
	
	@Test
	void getDiabetesRisk_Ok_Test() {
		
		// GIVEN 
		when(patientProxy.getPatient(anyInt())).thenReturn(patient1);
		when(noteProxy.getNotes(anyInt())).thenReturn(notes);
		when(riskService.calculateRisk(any(PatientBean.class), any(List.class))).thenReturn(diabetesRisk1);
		
		// WHEN 
		ResponseEntity<DiabetesRisk> result = diabetesController.getDiabetesRisk(1);
		
		// THEN 
		assertEquals(1, result.getBody().getPatientId());
		assertEquals(RiskLevel.NONE, result.getBody().getRisk());		
	}

}
