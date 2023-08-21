package com.medilabo.medilabofrontapp.bean;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

public class PatientBean {

	private int id;
	
	@NotBlank(message = "firstName cannot be empty")
	private String firstName;
	
	@NotBlank(message = "lastName cannot be empty")
	private String lastName;

	@Past
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate dateOfBirth;
	
	@NotBlank
	private String gender;
	
	private String address;
	
	private Long phoneNumber;

	public PatientBean() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "PatientBean{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", dateOfBirth='" + dateOfBirth + '\'' +
				", gender='" + gender + '\'' +
				", address=" + address + '\'' +
				", phoneNumber=" + phoneNumber +
				'}';
	}
}
