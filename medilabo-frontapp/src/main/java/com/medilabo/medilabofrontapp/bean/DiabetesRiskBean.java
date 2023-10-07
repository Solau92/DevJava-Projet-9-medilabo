package com.medilabo.medilabofrontapp.bean;

import com.medilabo.medilabofrontapp.model.RiskLevel;

public class DiabetesRiskBean {
	
	private int patientId;
	
	private RiskLevel risk;

	/**
	 * @return the patientId
	 */
	public int getPatientId() {
		return patientId;
	}

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return the risk
	 */
	public RiskLevel getRisk() {
		return risk;
	}

	/**
	 * @param risk the risk to set
	 */
	public void setRisk(RiskLevel risk) {
		this.risk = risk;
	}

	@Override
	public String toString() {
		return "DiabetesRiskBean [patientId=" + patientId + ", risk=" + risk + "]";
	}
	
}
