package com.medilabo.medilabonoteapp.integrationtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.medilabo.medilabonoteapp.entity.Note;
import com.medilabo.medilabonoteapp.service.implementation.NoteServiceImpl;
import org.springframework.util.StringUtils;

import net.minidev.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotesIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private NoteServiceImpl noteService;

	void setUp() {
		System.setProperty("spring.profiles.active", "test");
	}

	@Test
	@Order(1)
	void addNote_Test() throws Exception {

		JSONObject note1 = new JSONObject();
		note1.put("id", "111111");
		note1.put("patientId", "1");
		note1.put("date", "1981-01-11");
		note1.put("content", "content 1");

		String jsonNote1 = note1.toString();

		mockMvc.perform(post("/note/validate").contentType(MediaType.APPLICATION_JSON).content(jsonNote1))
				.andExpect(status().isCreated());

		JSONObject note2 = new JSONObject();
		note2.put("id", "222222");
		note2.put("patientId", "2");
		note2.put("date", "1982-02-22");
		note2.put("content", "content 2");

		String jsonNote2 = note2.toString();

		mockMvc.perform(post("/note/validate").contentType(MediaType.APPLICATION_JSON).content(jsonNote2))
				.andExpect(status().isCreated());

		Note note1Expected = noteService.findById("111111");
		Note note2Expected = noteService.findById("222222");

		assertEquals(1, note1Expected.getPatientId());
		assertEquals("content 2", note2Expected.getContent());
	}

	@Test
	@Order(2)
	void getNote_Test() throws Exception {

		JSONObject note3 = new JSONObject();
		note3.put("id", "333333");
		note3.put("patientId", "3");
		note3.put("date", "1983-03-03");
		note3.put("content", "content 3");

		String jsonNote3 = note3.toString();

		mockMvc.perform(post("/note/validate").contentType(MediaType.APPLICATION_JSON).content(jsonNote3))
				.andExpect(status().isCreated());

		JSONObject note4 = new JSONObject();
		note4.put("id", "444444");
		note4.put("patientId", "3");
		note4.put("date", "1984-04-04");
		note4.put("content", "content 4");

		String jsonNote4 = note4.toString();

		mockMvc.perform(post("/note/validate").contentType(MediaType.APPLICATION_JSON).content(jsonNote4))
				.andExpect(status().isCreated());

		MvcResult resultPatient3 = mockMvc.perform(get("/note/notes/3")).andExpect(status().isAccepted()).andReturn();

		String resultNote3AsString = resultPatient3.getResponse().getContentAsString();
		int numberOfTimesNote3 = StringUtils.countOccurrencesOf(resultNote3AsString, "date");

		assertEquals(2, numberOfTimesNote3);
		assertTrue(resultNote3AsString.contains("content 3"));
		assertTrue(resultNote3AsString.contains("content 4"));

		MvcResult resultNote1 = mockMvc.perform(get("/note/notes/1")).andExpect(status().isAccepted()).andReturn();

		String resultNote1AsString = resultNote1.getResponse().getContentAsString();
		int numberOfTimesNote1 = StringUtils.countOccurrencesOf(resultNote1AsString, "date");

		assertEquals(1, numberOfTimesNote1);
		assertTrue(resultNote1AsString.contains("content 1"));
		assertFalse(resultNote1AsString.contains("content 4"));
	}

	@Test
	@Order(3)
	void updateNote_Test() throws Exception {

		JSONObject note4Updated = new JSONObject();
		note4Updated.put("id", "444444");
		note4Updated.put("patientId", "3");
		note4Updated.put("date", "1984-04-04");
		note4Updated.put("content", "content 4 updated");

		String jsonNote4 = note4Updated.toString();

		mockMvc.perform(post("/note/validateUpdate").contentType(MediaType.APPLICATION_JSON).content(jsonNote4))
				.andExpect(status().isAccepted());

		Note note4Expected = noteService.findById("444444");

		assertEquals("content 4 updated", note4Expected.getContent());
	}

	@Test
	@Order(4)
	void deleteNote_Test() throws Exception {

		JSONObject note3 = new JSONObject();
		note3.put("id", "333333");
		note3.put("patientId", "3");
		note3.put("date", "1983-03-03");
		note3.put("content", "content 3");

		String jsonNote3 = note3.toString();

		mockMvc.perform(delete("/note/delete").contentType(MediaType.APPLICATION_JSON).content(jsonNote3))
				.andExpect(status().isAccepted());

		MvcResult result = mockMvc.perform(get("/note/notes/3")).andExpect(status().isAccepted()).andReturn();

		String resultAsString = result.getResponse().getContentAsString();
		int numberOfTimes = StringUtils.countOccurrencesOf(resultAsString, "patientId");

		assertEquals(1, numberOfTimes);
		assertTrue(resultAsString.contains("content 4 updated"));
		assertFalse(resultAsString.contains("content 3"));
	}

}
