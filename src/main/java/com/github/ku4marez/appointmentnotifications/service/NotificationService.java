package com.github.ku4marez.appointmentnotifications.service;

import com.github.ku4marez.appointmentnotifications.dto.CreateNotificationCommand;
import com.github.ku4marez.appointmentnotifications.dto.NotificationSentEvent;
import com.github.ku4marez.appointmentnotifications.endpoint.ClinicManagementClient;
import com.github.ku4marez.appointmentnotifications.endpoint.TwilioClient;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final ClinicManagementClient clinicManagementClient;
    private final CommandGateway commandGateway;
    private final EmailService emailService;
    private final TwilioClient twilioClient;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public NotificationService(ClinicManagementClient clinicManagementClient,
                               CommandGateway commandGateway,
                               EmailService emailService,
                               TwilioClient twilioClient, ApplicationEventPublisher eventPublisher) {
        this.clinicManagementClient = clinicManagementClient;
        this.commandGateway = commandGateway;
        this.emailService = emailService;
        this.twilioClient = twilioClient;
        this.eventPublisher = eventPublisher;
    }

    public void createNotificationForAppointment(AppointmentDTO appointmentDTO) {
        DoctorDTO doctor = fetchDoctorDetails(appointmentDTO.doctorId());
        PatientDTO patient = fetchPatientDetails(appointmentDTO.patientId());

        // Prepare and send email and SMS for doctor
        sendNotificationToDoctor(doctor, patient, appointmentDTO);

        // Prepare and send email and SMS for patient
        sendNotificationToPatient(doctor, patient, appointmentDTO);

        log.info("Notifications and Kafka events dispatched for doctor: {} and patient: {}",
                appointmentDTO.doctorId(), appointmentDTO.patientId());
    }

    private void sendNotificationToDoctor(DoctorDTO doctor, PatientDTO patient, AppointmentDTO appointmentDTO) {
        String doctorMessage = "A new appointment has been scheduled with patient: " +
                patient.firstName() + " " + patient.lastName();

        CreateNotificationCommand doctorCommand = new CreateNotificationCommand(
                appointmentDTO.doctorId(),
                null, // Patient ID not relevant for doctor's notification
                doctorMessage,
                NotificationStatus.PENDING
        );
        NotificationEntity notificationEntity = commandGateway.sendAndWait(doctorCommand);

        // Send SMS to doctor
        if (doctor.phoneNumber() != null) {
            twilioClient.sendSMS(doctor.phoneNumber(), doctorMessage);
            log.info("SMS sent to doctor: {}", doctor.phoneNumber());
        }

        // Send email to doctor
        if (doctor.email() != null) {
            emailService.sendAppointmentEmailToDoctor(doctor, patient, appointmentDTO);
            log.info("Email sent to doctor: {}", doctor.email());
        }
        eventPublisher.publishEvent(new NotificationSentEvent(this, notificationEntity));

    }

    private void sendNotificationToPatient(DoctorDTO doctor, PatientDTO patient, AppointmentDTO appointmentDTO) {
        String patientMessage = "Your appointment has been scheduled with Dr. " +
                doctor.firstName() + " " + doctor.lastName();
        // Dispatch notification command for patient's records
        CreateNotificationCommand patientCommand = new CreateNotificationCommand(
                null, // Doctor ID not relevant for patient's notification
                appointmentDTO.patientId(),
                patientMessage,
                NotificationStatus.PENDING
        );
        NotificationEntity notificationEntity = commandGateway.sendAndWait(patientCommand);

        // Send SMS to patient
        if (patient.phoneNumber() != null) {
            twilioClient.sendSMS(patient.phoneNumber(), patientMessage);
            log.info("SMS sent to patient: {}", patient.phoneNumber());
        }

        // Send email to patient
        if (patient.email() != null) {
            emailService.sendAppointmentEmailToPatient(doctor, patient, appointmentDTO);
            log.info("Email sent to patient: {}", patient.email());
        }
        eventPublisher.publishEvent(new NotificationSentEvent(this, notificationEntity));
    }

    private DoctorDTO fetchDoctorDetails(String doctorId) {
        return clinicManagementClient.getDoctorById(doctorId);
    }

    private PatientDTO fetchPatientDetails(String patientId) {
        return clinicManagementClient.getPatientById(patientId);
    }
}
