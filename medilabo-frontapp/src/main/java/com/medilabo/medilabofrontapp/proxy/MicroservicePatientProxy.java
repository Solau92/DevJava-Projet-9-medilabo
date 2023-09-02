package com.medilabo.medilabofrontapp.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.medilabo.medilabofrontapp.bean.PatientBean;

@FeignClient(name = "microservice-gateway", url = "localhost:8081")
public interface MicroservicePatientProxy {

	@GetMapping("/ms-patient/patient/patients")
	List<PatientBean> patients();

	@GetMapping("/ms-patient/patient/{id}")
	PatientBean getPatient(@PathVariable("id") int id);

	@PostMapping("/ms-patient/patient/validate")
	PatientBean addPatient(@RequestBody PatientBean patient);

	@PostMapping("/ms-patient/patient/validateUpdate")
	PatientBean updatePatient(@RequestBody PatientBean patient);

	@DeleteMapping("/ms-patient/patient/delete")
	void deletePatient(@RequestBody PatientBean patient);
	
}
