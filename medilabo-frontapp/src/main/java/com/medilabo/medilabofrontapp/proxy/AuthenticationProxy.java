package com.medilabo.medilabofrontapp.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "microservice-gateway", url = "localhost:8081", contextId = "authentication")
public interface AuthenticationProxy {
	
	@GetMapping("/auth/login")
	void login(@RequestHeader("Authorization") String header);
	
}
