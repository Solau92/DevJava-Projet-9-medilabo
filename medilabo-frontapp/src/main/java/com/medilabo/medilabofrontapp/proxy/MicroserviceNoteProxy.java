package com.medilabo.medilabofrontapp.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.medilabo.medilabofrontapp.bean.NoteBean;
import com.medilabo.medilabofrontapp.bean.PatientBean;

@FeignClient(name = "microservice-gateway", url = "${microservice-gateway.url}", contextId = "microservice-note")
public interface MicroserviceNoteProxy {
	
	@PostMapping("/ms-note/note/validate")
	NoteBean addNote(@RequestHeader("Authorization") String header, @RequestBody NoteBean note);

	@GetMapping("/ms-note/note/{id}")
	NoteBean getNote(@RequestHeader("Authorization") String header, @PathVariable("id") String id);
	
	@GetMapping("/ms-note/note/notes/{patientId}")
	List<NoteBean> getNotes(@RequestHeader("Authorization") String header, @PathVariable("patientId") int patientId);

	@PostMapping("/ms-note/note/validateUpdate")
	NoteBean updateNote(@RequestHeader("Authorization") String header, @RequestBody NoteBean note);
	
	@DeleteMapping("/ms-note/note/delete")
	void deleteNote(@RequestHeader("Authorization") String header, @RequestBody NoteBean note);
}
