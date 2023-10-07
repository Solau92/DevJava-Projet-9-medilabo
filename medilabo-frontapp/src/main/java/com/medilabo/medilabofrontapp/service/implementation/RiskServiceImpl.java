package com.medilabo.medilabofrontapp.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.proxy.MicroserviceRiskProxy;
import com.medilabo.medilabofrontapp.service.RiskService;

@Service
public class RiskServiceImpl implements RiskService {

	private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

	private MicroserviceRiskProxy riskProxy;

	private static Context context;

	public RiskServiceImpl(MicroserviceRiskProxy riskProxy, Context context) {
		this.riskProxy = riskProxy;
		this.context = context;
	}
	
	@Override
	public DiabetesRiskBean getDiabetesRisk(String header, int patientId) {

		DiabetesRiskBean diabetesRisk = new DiabetesRiskBean();
		diabetesRisk = riskProxy.getDiabetesRisk(header, patientId);
		// TODO : gérer exceptions 
		return diabetesRisk;
	}

}
