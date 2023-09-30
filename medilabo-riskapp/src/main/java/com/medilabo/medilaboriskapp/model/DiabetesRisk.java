package com.medilabo.medilaboriskapp.model;

public class DiabetesRisk {
	
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

}
