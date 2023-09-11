package com.medilabo.medilabofrontapp.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.medilabo.medilabofrontapp.bean.PatientBean;

@FeignClient(name = "microservice-gateway", url = "localhost:8081", contextId = "microservice-gateway")
public interface MicroservicePatientProxy {
	
	@GetMapping("/ms-patient/patient/patients")
	List<PatientBean> patients(@RequestHeader("Authorization") String header);
	
	@GetMapping("/ms-patient/patient/{id}")
	PatientBean getPatient(@RequestHeader("Authorization") String header, @PathVariable("id") int id);

	@PostMapping("/ms-patient/patient/validate")
	PatientBean addPatient(@RequestHeader("Authorization") String header, @RequestBody PatientBean patient);

	@PostMapping("/ms-patient/patient/validateUpdate")
	PatientBean updatePatient(@RequestHeader("Authorization") String header, @RequestBody PatientBean patient);

	@DeleteMapping("/ms-patient/patient/delete")
	void deletePatient(@RequestHeader("Authorization") String header, @RequestBody PatientBean patient);
	
}
