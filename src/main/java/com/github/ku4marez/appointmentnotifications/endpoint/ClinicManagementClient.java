package com.github.ku4marez.appointmentnotifications.endpoint;

import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClinicManagementClient {

    private final RestTemplate restTemplate;

    @Autowired
    public ClinicManagementClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Cacheable("doctors")
    public DoctorDTO getDoctorById(String id) {
        String url = "http://localhost:8080/api/doctors/" + id;
        return restTemplate.getForObject(url, DoctorDTO.class);
    }

    @Cacheable("patients")
    public PatientDTO getPatientById(String id) {
        String url = "http://localhost:8080/api/patients/" + id;
        return restTemplate.getForObject(url, PatientDTO.class);
    }
}
