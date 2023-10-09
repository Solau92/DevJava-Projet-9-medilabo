package com.medilabo.medilabopatientapp.service.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.medilabo.medilabopatientapp.entity.Patient;
import com.medilabo.medilabopatientapp.exception.PatientAlreadyExistsException;
import com.medilabo.medilabopatientapp.exception.PatientNotFoundException;
import com.medilabo.medilabopatientapp.repository.PatientRepository;
import com.medilabo.medilabopatientapp.service.PatientService;

@Service 
public class PatientServiceImpl implements PatientService {

	private PatientRepository patientRepository;

	private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

	public PatientServiceImpl(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	
	/**
	 * Returns a list of all the patients in database.
	 * 
	 * @return List<Patient>
	 */
	@Override
	public List<Patient> findAll() {
		log.info("Find all patients");
		return patientRepository.findAll();
	}

	/**
	 * Returns the patient found in database given its id.
	 * 
	 * @param id
	 * @return Patient
	 * @throws PatientNotFoundException if no patient found with the given id
	 */
	@Override
	public Patient findById(Integer id) throws PatientNotFoundException {

		log.info("Search patient whith id {}", id);
		Optional<Patient> optionalPatient = patientRepository.findById(id);

		if (optionalPatient.isEmpty()) {
			log.error("Patient with id {} not found", id);
			throw new PatientNotFoundException("Patient with id {} " + id + " not found");
		}
		return optionalPatient.get();
	}

	/**
	 * Saves in database and return the given patient.
	 * 
	 * @param patient
	 * @return Patient
	 * @throws PatientAlreadyExistsException if a patient already exists with the
	 *                                       same firstName, lastName and
	 *                                       dateOfBirth
	 */
	@Override
	public Patient save(Patient patient) throws PatientAlreadyExistsException {

		log.info("Trying to save patient named {}{}", patient.getFirstName(), patient.getLastName());

		Optional<Patient> optionalPatient = patientRepository.findByFirstNameAndLastNameAndDateOfBirth(patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());

		if (optionalPatient.isPresent()) {
			log.error("Patient named {}{} born the {} already exists", patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());
			throw new PatientAlreadyExistsException("Patient named " + patient.getFirstName() + "  " + patient.getLastName() + " born the " + patient.getDateOfBirth() + " already exists");
		}
		log.info("Patient named {}{} successfully saved", patient.getFirstName(), patient.getLastName());
		return patientRepository.save(patient);
	}

	/**
	 * Updates in database and returns the patient.
	 * 
	 * @param patient
	 * @return Patient
	 * @throws PatientNotFoundException      if no patient found with the patient's
	 *                                       id
	 * @throws PatientAlreadyExistsException if a patient already exists with the
	 *                                       same firstName, lastName and
	 *                                       dateOfBirth
	 */
	@Override
	public Patient update(Patient patient) throws PatientNotFoundException, PatientAlreadyExistsException {

		log.info("Trying to update patient named {}{}", patient.getFirstName(), patient.getLastName());

		Optional<Patient> optionalPatientId = patientRepository.findById(patient.getId());

		if (optionalPatientId.isEmpty()) {
			log.error("Patient with id {} not found", patient.getId());
			throw new PatientNotFoundException("Patient with id " + patient.getId() + " not found");
		}

		Optional<Patient> optionalPatient = patientRepository.findByFirstNameAndLastNameAndDateOfBirth(patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());

		if (optionalPatient.isPresent()) {
			if (patient.getId() != optionalPatient.get().getId()) {
				log.error("Patient named {}{} born the {} already exists", patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());
				throw new PatientAlreadyExistsException("Patient named " + patient.getFirstName() + "  " + patient.getLastName() + " born the " + patient.getDateOfBirth() + " already exists");
			}
		}

		log.info("Patient named {}{} successfully updated", patient.getFirstName(), patient.getLastName());

		return patientRepository.save(patient);
	}

	/**
	 * Deletes in database the given patient.
	 * 
	 * @param patient
	 * @throws PatientNotFoundException if the patient was not found 
	 */
	@Override
	public void delete(Patient patient) throws PatientNotFoundException {

		log.info("Trying to delete patient named {}{}", patient.getFirstName(), patient.getLastName());

		Optional<Patient> optionalPatient = patientRepository.findById(patient.getId());

		if (optionalPatient.isEmpty()) {
			log.error("Patient named {}{} born the {} was not found", patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());
			throw new PatientNotFoundException("Patient named " + patient.getFirstName() + " " + patient.getLastName() + " born the " + patient.getDateOfBirth() + " not found");
		}

		log.info("Patient named {}{} successfully deleted", patient.getFirstName(), patient.getLastName());

		patientRepository.delete(patient);
	}
}
