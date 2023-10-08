package com.medilabo.medilabofrontapp.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabofrontapp.bean.DiabetesRiskBean;
import com.medilabo.medilabofrontapp.constants.HTMLPage;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.proxy.MicroserviceRiskProxy;
import com.medilabo.medilabofrontapp.service.RiskService;

import feign.FeignException;

@Service
public class RiskServiceImpl implements RiskService {

	private static final Logger log = LoggerFactory.getLogger(RiskServiceImpl.class);

	private MicroserviceRiskProxy riskProxy;

	private static Context context;

	public RiskServiceImpl(MicroserviceRiskProxy riskProxy, Context context) {
		this.riskProxy = riskProxy;
		this.context = context;
	}

	/**
	 * Returns the diabetes risk of a patient, given his id, from the risk app.
	 * 
	 * @param header    corresponding to authorization header
	 * @param patientId
	 * @return DiabetesRiskBean
	 */
	@Override
	public DiabetesRiskBean getDiabetesRisk(String header, int patientId) {

		DiabetesRiskBean diabetesRisk = new DiabetesRiskBean();

		try {
			diabetesRisk = riskProxy.getDiabetesRisk(header, patientId);
			log.info(diabetesRisk.toString());

		} catch (FeignException e) { // TODO : gérer exceptions
			log.info("FeignException status : {}, message : {}", e.status(), e.getMessage());

			if (e.status() == 401) {
				context.setRedirectAfterExceptionUrl(HTMLPage.PATIENTS);
				context.setReturnUrl(HTMLPage.HOME);
			}
		}
		return diabetesRisk;
	}

}
