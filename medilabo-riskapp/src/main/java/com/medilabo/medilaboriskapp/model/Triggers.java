package com.medilabo.medilaboriskapp.model;

public enum Triggers {

	HEMOGLOBINE_A1C("Hémoglobine A1C"),  
	MICROALBUMINE("Microalbumine"),
	TAILLE("Taille"),
	POIDS("Poids"),
	FUMEUR("Fumeur"),
	FUMEUSE("Fumeuse"),
	ANORMAL("Anormal"),
	CHOLESTEROL("Cholestérol"),
	VERTIGES("Vertiges"),
	RECHUTE("Rechute"),
	REACTION("Réaction"),
	ANTICORPS("Anticorps");

	public final String label;

	private Triggers(String label) {
		this.label = label;
	}

}
