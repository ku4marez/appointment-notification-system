package com.github.ku4marez.appointmentnotifications.service;

import com.github.ku4marez.appointmentnotifications.endpoint.TwilioClient;
import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSService {

    private final TwilioClient twilioClient;

    @Autowired
    public SMSService(TwilioClient twilioClient) {
        this.twilioClient = twilioClient;
    }

    public void sendAppointmentSMSToDoctor(DoctorDTO doctor, PatientDTO patient, AppointmentDTO appointment) {
        String message = String.format("Dr. %s %s, you have a new appointment with patient %s %s on %s.",
                doctor.firstName(), doctor.lastName(), patient.firstName(), patient.lastName(), appointment.dateTime());
        sendSMS(doctor.phoneNumber(), message);
    }

    public void sendAppointmentSMSToPatient(DoctorDTO doctor, PatientDTO patient, AppointmentDTO appointment) {
        String message = String.format("Dear %s %s, your appointment with Dr. %s %s has been scheduled on %s.",
                patient.firstName(), patient.lastName(), doctor.firstName(), doctor.lastName(), appointment.dateTime());
        sendSMS(patient.phoneNumber(), message);
    }

    private void sendSMS(String to, String message) {
        try {
            twilioClient.sendSMS(to, message);
            log.info("SMS sent to {}: {}", to, message);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", to, e.getMessage());
        }
    }
}

