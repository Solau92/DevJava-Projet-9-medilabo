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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
	
	@MockBean
	MicroservicePatientProxy patientProxy;
	
	@MockBean
	MicroserviceNoteProxy noteProxy;
	
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
		patient1.setDateOfBirth(LocalDate.now().minusYears(20));
		patient1.setGender("F");
				
		note1 = new NoteBean();
		note1.setId("111111");
		note1.setDate(LocalDate.now());
		note1.setContent("two triggers poids taille");
		note1.setPatientId(1);

		note2 = new NoteBean();
		note2.setId("222222");
		note2.setDate(LocalDate.now());
		note2.setContent("two triggers fumeuse rechute");
		note2.setPatientId(1);

		notes = new ArrayList<>();
		notes.add(note1);
		notes.add(note2);
	}
	
	@Test
	void getDiabetesRisk_Test() throws Exception {
			
		when(patientProxy.getPatient(anyInt())).thenReturn(patient1);
		when(noteProxy.getNotes(anyInt())).thenReturn(notes);
		
		MvcResult result = mockMvc.perform(get("/risk/1")).
				andExpect(status().isAccepted()).andReturn();
		
		String resultAsString = result.getResponse().getContentAsString();
					
		assertTrue(resultAsString.contains("IN_DANGER"));
			
	}
}
