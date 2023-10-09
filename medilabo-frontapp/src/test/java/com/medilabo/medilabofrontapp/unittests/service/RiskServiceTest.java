package com.medilabo.medilabofrontapp.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.model.RiskLevel;
import com.medilabo.medilabofrontapp.proxy.MicroserviceRiskProxy;
import com.medilabo.medilabofrontapp.service.implementation.RiskServiceImpl;

@SpringBootTest
class RiskServiceTest {
	
	@InjectMocks
	RiskServiceImpl riskService;
	
	@Mock
	MicroserviceRiskProxy riskProxy;
	
	@Mock
	Context context;
	
	DiabetesRiskBean diabetesRisk; 
	String header;
	
	@BeforeEach
	void setUp() {
		
		header = "ok";

		diabetesRisk = new DiabetesRiskBean();
		diabetesRisk.setPatientId(1);
		diabetesRisk.setRisk(RiskLevel.NONE);
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
	
	@Disabled
	@Test
	void getDiabetesRisk_Forbidden_Test() {
		
//		// GIVEN
//		when(riskProxy.getDiabetesRisk(any(String.class), anyInt())).thenReturn(diabetesRisk);
//
//		// WHEN
//		DiabetesRiskBean result = riskService.getDiabetesRisk(header, 1);
//
//		// THEN
//		assertEquals(diabetesRisk.getRisk(), result.getRisk());
	}

}
