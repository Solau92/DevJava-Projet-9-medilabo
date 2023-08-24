package com.medilabo.medilabopatientapp.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.medilabo.medilabopatientapp.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer>, JpaSpecificationExecutor<Patient> {

	/**
	 * Returns an Optional containing the patient found given its firstName,
	 * lastName and dateOfBirth, and an empty Optional if no patient found.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param dateOfBirth
	 * @return Optional<Patient>
	 */
	public Optional<Patient> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName,
			LocalDate dateOfBirth);
}
