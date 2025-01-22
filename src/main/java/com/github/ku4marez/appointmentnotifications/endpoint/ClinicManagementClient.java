package com.github.ku4marez.appointmentnotifications.endpoint;

import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClinicManagementClient {

    @Value("${clinic.management.api.doctors}")
    private String clinicManagementDoctorsApi;

    @Value("${clinic.management.api.patients}")
    private String clinicManagementPatientsApi;

    private final RestTemplate restTemplate;

    @Autowired
    public ClinicManagementClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Cacheable("doctors")
    public DoctorDTO getDoctorById(String id) {
        String url = clinicManagementDoctorsApi + id;
        return restTemplate.getForObject(url, DoctorDTO.class);
    }

    @Cacheable("patients")
    public PatientDTO getPatientById(String id) {
        String url = clinicManagementPatientsApi + id;
        return restTemplate.getForObject(url, PatientDTO.class);
    }
}
