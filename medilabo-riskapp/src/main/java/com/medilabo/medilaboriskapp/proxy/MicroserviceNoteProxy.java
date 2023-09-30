package com.medilabo.medilaboriskapp.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.medilabo.medilaboriskapp.bean.NoteBean;

@FeignClient(name = "microservice-note", url = "${microservice-note.url}", contextId = "microservice-note")
public interface MicroserviceNoteProxy {
	
	@GetMapping("/note/notes/{patientId}")
	List<NoteBean> getNotes(@RequestHeader("Authorization") String header, @PathVariable("patientId") int patientId);

}
