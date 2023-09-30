package com.medilabo.medilabofrontapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.medilabofrontapp.bean.PatientBean;
import com.medilabo.medilabofrontapp.context.Context;
import com.medilabo.medilabofrontapp.proxy.MicroservicePatientProxy;

import feign.FeignException;

@Service
public class PatientService {
	
//	private MicroservicePatientProxy patientProxy;
//	
//	private static Context context;
//
//	public PatientService(MicroservicePatientProxy patientProxy, Context context) {
//		this.patientProxy = patientProxy;
//		this.context = context;
//	}
//	
//	public List<PatientBean> getPatients(String authHeader) {
//		
//		List<PatientBean> patients = new ArrayList<>();
//		
//		try {
//			patients = patientProxy.patients(authHeader);
//			context.resetUrl();
//			context.resetPatientId();
//			
//		} catch (FeignException e) {
//			
//			if (e.status() == 401) {
//				context.setUrl("/patient/patients");
//		}
//		
//		return patients;
//	}
//	
//	

}
