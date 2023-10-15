package com.medilabo.medilabofrontapp.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.RiskLevel;
import com.medilabo.medilabofrontapp.proxy.MicroserviceRiskProxy;
import com.medilabo.medilabofrontapp.service.implementation.RiskServiceImpl;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class RiskServiceTest {
	
	@InjectMocks
	RiskServiceImpl riskService;
	
	@Mock
	MicroserviceRiskProxy riskProxy;
	
	@Mock
	Context context;
	
	LogCaptor logCaptor = LogCaptor.forClass(RiskServiceImpl.class);

	DiabetesRiskBean diabetesRisk; 
	String header;
	FeignException unauthorizedException;

	
	@BeforeEach
	void setUp() {
		
		header = "ok";

		diabetesRisk = new DiabetesRiskBean();
		diabetesRisk.setPatientId(1);
		diabetesRisk.setRisk(RiskLevel.NONE);
		
		unauthorizedException = new FeignException.Unauthorized("", Request.create(HttpMethod.GET, "", new HashMap(), new byte[0], Charset.defaultCharset()), new byte[0], new HashMap<>()); 
	}
	
	@Test
	void getDiabetesRisk_Ok_Test() {
		
		// GIVEN
		when(riskProxy.getDiabetesRisk(any(String.class), anyInt())).thenReturn(diabetesRisk);

		// WHEN
		DiabetesRiskBean result = riskService.getDiabetesRisk(header, 1);

		// THEN
		assertEquals(diabetesRisk.getRisk(), result.getRisk());
	}
	
	@Test
	void getDiabetesRisk_Forbidden_Test() {
		
		// GIVEN
		when(riskProxy.getDiabetesRisk(any(String.class), anyInt())).thenThrow(unauthorizedException);

		// WHEN
		riskService.getDiabetesRisk(header, 1);

		// THEN
		assertTrue(logCaptor.getLogEvents().get(0).getFormattedMessage().contains("FeignException status :"));
		assertTrue(logCaptor.getLogEvents().get(1).getFormattedMessage().contains("Exception 401"));
	}

}
