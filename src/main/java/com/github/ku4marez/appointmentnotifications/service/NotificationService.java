package com.github.ku4marez.appointmentnotifications.service;

import com.github.ku4marez.appointmentnotifications.dto.CreateNotificationCommand;
import com.github.ku4marez.appointmentnotifications.endpoint.ClinicManagementClient;
import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;
import com.github.ku4marez.commonlibraries.util.KafkaProducerUtil;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final KafkaProducerUtil kafkaProducerUtil;
    private final ClinicManagementClient clinicManagementClient;
    private final CommandGateway commandGateway;

    @Autowired
    public NotificationService(KafkaProducerUtil kafkaProducerUtil,
                               ClinicManagementClient clinicManagementClient,
                               CommandGateway commandGateway) {
        this.kafkaProducerUtil = kafkaProducerUtil;
        this.clinicManagementClient = clinicManagementClient;
        this.commandGateway = commandGateway;
    }

    public void createNotificationForAppointment(AppointmentDTO appointmentDTO) {
        DoctorDTO doctor = fetchDoctorDetails(appointmentDTO.doctorId());
        PatientDTO patient = fetchPatientDetails(appointmentDTO.patientId());

        // Dispatch command for doctor's notification
        CreateNotificationCommand doctorCommand = new CreateNotificationCommand(
                appointmentDTO.doctorId(),
                null, // Patient ID not relevant for doctor's notification
                "A new appointment has been scheduled with patient: " + patient.firstName() + " " + patient.lastName(),
                NotificationStatus.PENDING
        );
        commandGateway.send(doctorCommand);

        // Dispatch command for patient's notification
        CreateNotificationCommand patientCommand = new CreateNotificationCommand(
                null, // Doctor ID not relevant for patient's notification
                appointmentDTO.patientId(),
                "Your appointment has been scheduled with Dr. " + doctor.firstName() + " " + doctor.lastName(),
                NotificationStatus.PENDING
        );
        commandGateway.send(patientCommand);

        log.info("Notification commands dispatched for doctor: {} and patient: {}",
                appointmentDTO.doctorId(), appointmentDTO.patientId());
    }

    private DoctorDTO fetchDoctorDetails(String doctorId) {
        return clinicManagementClient.getDoctorById(doctorId);
    }

    private PatientDTO fetchPatientDetails(String patientId) {
        return clinicManagementClient.getPatientById(patientId);
    }
}
