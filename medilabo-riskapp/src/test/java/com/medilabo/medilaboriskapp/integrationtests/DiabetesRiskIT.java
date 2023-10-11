package com.medilabo.medilaboriskapp.integrationtests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.medilabo.medilaboriskapp.bean.NoteBean;
import com.medilabo.medilaboriskapp.bean.PatientBean;
import com.medilabo.medilaboriskapp.proxy.MicroserviceNoteProxy;
import com.medilabo.medilaboriskapp.proxy.MicroservicePatientProxy;
import com.medilabo.medilaboriskapp.service.implementation.DiabetesRiskServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
class DiabetesRiskIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DiabetesRiskServiceImpl diabetesRiskService;
	
//	@Mock
//	MicroservicePatientProxy patientProxy;
//	
//	@Mock
//	MicroserviceNoteProxy noteProxy;
	
	PatientBean patient1;
	List<NoteBean> notes;
	NoteBean note1;
	NoteBean note2;
	
	@BeforeEach
	void setUp() {
		
		patient1 = new PatientBean();
		patient1.setId(1);
		patient1.setFirstName("firstName1");
		patient1.setLastName("lastName1");
		patient1.setDateOfBirth(LocalDate.of(1981, 01, 01));
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
	}
	
	@Test
	void getDiabetesRisk_Test() throws Exception {
			
		// Commment faire pour requête autres microservices ?? 
//		when(patientProxy.getPatient(anyInt())).thenReturn(patient1);
//		when(noteProxy.getNotes(anyInt())).thenReturn(notes);
		
//		jakarta.servlet.ServletException: Request processing failed: 
//		feign.RetryableException: Connection refused: no further information executing 
//		GET http://localhost:8082/patient/1
		
		MvcResult result = mockMvc.perform(get("/risk/1")).
				andExpect(status().isAccepted()).andReturn();
		
		int resultStatus = result.getResponse().getStatus();	
			
	}
}
