package com.medilabo.medilaboriskapp.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.medilabo.medilaboriskapp.bean.PatientBean;

@FeignClient(name = "microservice-patient", url = "${microservice-patient.url}", contextId = "microservice-patient")
public interface MicroservicePatientProxy {
	
	@GetMapping("/patient/{id}")
	PatientBean getPatient(@RequestHeader("Authorization") String header, @PathVariable("id") int id);

}
