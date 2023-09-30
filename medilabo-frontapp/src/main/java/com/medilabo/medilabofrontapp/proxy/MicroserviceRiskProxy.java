package com.medilabo.medilabofrontapp.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;

@FeignClient(name = "microservice-gateway", url = "${microservice-gateway.url}", contextId = "microservice-risk")
public interface MicroserviceRiskProxy {
	
	@GetMapping("/ms-risk/risk/{patientId}")
	DiabetesRiskBean getDiabetesRisk(@RequestHeader("Authorization") String header, @PathVariable("patientId") int patientId);

}
