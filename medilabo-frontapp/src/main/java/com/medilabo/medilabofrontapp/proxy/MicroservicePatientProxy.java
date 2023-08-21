package com.medilabo.medilabofrontapp.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.medilabo.medilabofrontapp.bean.PatientBean;

@FeignClient(name = "microservice-patient", url = "localhost:8081")
public interface MicroservicePatientProxy {

	@GetMapping(value = "/patients")
	List<PatientBean> patients();

	@GetMapping(value = "/patient/{id}")
	PatientBean getPatient(@PathVariable("id") int id);

	@PostMapping(value = "/patient/validate")
	PatientBean addPatient(@RequestBody PatientBean patient);

	@PostMapping("/patient/validateUpdate")
	PatientBean updatePatient(@RequestBody PatientBean patient);

	@DeleteMapping("/patient/delete")
	void deletePatient(@RequestBody PatientBean patient);
	
}
