package com.medilabo.medilabopatientapp.integrationtests;

import com.medilabo.medilabopatientapp.entity.Patient;
import com.medilabo.medilabopatientapp.service.implementation.PatientServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;

import org.junit.jupiter.api.*;

import net.minidev.json.JSONObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PatientServiceImpl patientService;

	@Test
	@Order(1)
	void addPatient_Test() throws Exception {

		JSONObject patient1 = new JSONObject();
		patient1.put("firstName", "firstName1");
		patient1.put("lastName", "lastName1");
		patient1.put("dateOfBirth", "1981-01-11");
		patient1.put("gender", "M");
		patient1.put("phoneNumber", "0123456789");
		patient1.put("address", "address1");

		String jsonPatient1 = patient1.toString();

		mockMvc.perform(post("/patient/validate").contentType(MediaType.APPLICATION_JSON).content(jsonPatient1))
				.andExpect(status().isCreated());

		JSONObject patient2 = new JSONObject();
		patient2.put("firstName", "firstName2");
		patient2.put("lastName", "lastName2");
		patient2.put("dateOfBirth", "1982-02-02");
		patient2.put("gender", "F");
		patient2.put("phoneNumber", "0123456789");
		patient2.put("address", "address2");

		String jsonPatient2 = patient2.toString();

		mockMvc.perform(post("/patient/validate").contentType(MediaType.APPLICATION_JSON).content(jsonPatient2))
				.andExpect(status().isCreated());

		Patient patient1Expected = patientService.findById(1);
		Patient patient2Expected = patientService.findById(2);

		assertEquals("firstName1", patient1Expected.getFirstName());
		assertEquals("lastName2", patient2Expected.getLastName());
	}

	@Test
	@Order(2)
	void getPatient_Test() throws Exception {

		JSONObject patient3 = new JSONObject();
		patient3.put("firstName", "firstName3");
		patient3.put("lastName", "lastName3");
		patient3.put("dateOfBirth", "1983-03-03");
		patient3.put("gender", "M");
		patient3.put("phoneNumber", "0123456789");
		patient3.put("address", "address3");

		String jsonPatient3 = patient3.toString();

		mockMvc.perform(post("/patient/validate").contentType(MediaType.APPLICATION_JSON).content(jsonPatient3))
				.andExpect(status().isCreated());

		JSONObject patient4 = new JSONObject();
		patient4.put("firstName", "firstName4");
		patient4.put("lastName", "lastName4");
		patient4.put("dateOfBirth", "1983-04-04");
		patient4.put("gender", "M");
		patient4.put("phoneNumber", "0123456789");
		patient4.put("address", "address4");

		String jsonPatient4 = patient4.toString();

		mockMvc.perform(post("/patient/validate").contentType(MediaType.APPLICATION_JSON).content(jsonPatient4))
				.andExpect(status().isCreated());

		MvcResult result = mockMvc.perform(get("/patient/patients")).
				andExpect(status().isAccepted()).andReturn();

		String resultAsString = result.getResponse().getContentAsString();
		int numberOfTimes = StringUtils.countOccurrencesOf(resultAsString, "dateOfBirth");
		
		assertEquals(4, numberOfTimes);
		assertTrue(resultAsString.contains("firstName1"));
		assertTrue(resultAsString.contains("firstName2"));
		assertTrue(resultAsString.contains("firstName3"));
		assertTrue(resultAsString.contains("firstName4"));
	}
	
	@Test
	@Order(3)
	void updatePatient_Test() throws Exception {

		JSONObject patient4 = new JSONObject();
		patient4.put("id", "4");
		patient4.put("firstName", "firstName4");
		patient4.put("lastName", "lastName4");
		patient4.put("dateOfBirth", "1983-04-04");
		patient4.put("gender", "M");
		patient4.put("phoneNumber", "0123456789");
		patient4.put("address", "address4updated");

		String jsonPatient4 = patient4.toString();

		mockMvc.perform(post("/patient/validateUpdate").contentType(MediaType.APPLICATION_JSON).content(jsonPatient4))
				.andExpect(status().isAccepted());
		
		Patient patient4Expected = patientService.findById(4);

		assertEquals("address4updated", patient4Expected.getAddress());		
	}
	
	@Test
	@Order(4)
	void deletePatient_Test() throws Exception {

		JSONObject patient4 = new JSONObject();
		patient4.put("id", "4");
		patient4.put("firstName", "firstName4");
		patient4.put("lastName", "lastName4");
		patient4.put("dateOfBirth", "1983-04-04");
		patient4.put("gender", "M");
		patient4.put("phoneNumber", "0123456789");
		patient4.put("address", "address4updated");

		String jsonPatient4 = patient4.toString();

		mockMvc.perform(delete("/patient/delete").contentType(MediaType.APPLICATION_JSON).content(jsonPatient4))
				.andExpect(status().isAccepted());
		
		MvcResult result = mockMvc.perform(get("/patient/patients")).
				andExpect(status().isAccepted()).andReturn();

		String resultAsString = result.getResponse().getContentAsString();
		int numberOfTimes = StringUtils.countOccurrencesOf(resultAsString, "dateOfBirth");
		
		assertEquals(3, numberOfTimes);
		assertTrue(resultAsString.contains("firstName1"));
		assertTrue(resultAsString.contains("firstName2"));
		assertTrue(resultAsString.contains("firstName3"));
		assertFalse(resultAsString.contains("firstName4"));
	}

}
