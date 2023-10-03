package com.medilabo.medilaboriskapp.model;

public enum Triggers {

	HEMOGLOBINE_A1C("hémoglobine A1C"),  
	MICROALBUMINE("microalbumine"),
	TAILLE("taille"),
	POIDS("poids"),
	FUMEUR("fumeur"),
	FUMEUSE("fumeuse"),
	ANORMAL("anormal"),
	CHOLESTEROL("cholestérol"),
	VERTIGES("vertiges"),
	RECHUTE("rechute"),
	REACTION("réaction"),
	ANTICORPS("anticorps");

	public final String label;

	private Triggers(String label) {
		this.label = label;
	}

}
