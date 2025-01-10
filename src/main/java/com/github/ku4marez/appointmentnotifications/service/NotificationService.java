package com.github.ku4marez.appointmentnotifications.service;

import com.github.ku4marez.appointmentnotifications.endpoint.ClinicManagementClient;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.appointmentnotifications.repository.NotificationRepository;
import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;
import com.github.ku4marez.commonlibraries.util.KafkaProducerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final KafkaProducerUtil kafkaProducerUtil;
    private final ClinicManagementClient clinicManagementClient;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               KafkaProducerUtil kafkaProducerUtil,
                               ClinicManagementClient clinicManagementClient) {
        this.notificationRepository = notificationRepository;
        this.kafkaProducerUtil = kafkaProducerUtil;
        this.clinicManagementClient = clinicManagementClient;
    }

    public void createNotificationForAppointment(AppointmentDTO appointmentDTO) {
        DoctorDTO doctor = clinicManagementClient.getDoctorById(appointmentDTO.doctorId());

        PatientDTO patient = clinicManagementClient.getPatientById(appointmentDTO.patientId());

        // Create notification for doctor
        NotificationEntity doctorNotification = new NotificationEntity();
        doctorNotification.setDoctorId(appointmentDTO.doctorId());
        doctorNotification.setPatientId(null); // Not relevant for doctor notification
        doctorNotification.setContent("A new appointment has been scheduled with patient: " + patient.firstName() + " " + patient.lastName());
        doctorNotification.setStatus(NotificationStatus.PENDING);
        notificationRepository.save(doctorNotification);

        // Create notification for patient
        NotificationEntity patientNotification = new NotificationEntity();
        patientNotification.setDoctorId(null); // Not relevant for patient notification
        patientNotification.setPatientId(appointmentDTO.patientId());
        patientNotification.setContent("Your appointment has been scheduled with Dr. " + doctor.firstName() + " " + doctor.lastName());
        patientNotification.setStatus(NotificationStatus.PENDING);
        notificationRepository.save(patientNotification);

        log.info("Notifications created for doctor: {} and patient: {}", appointmentDTO.doctorId(), appointmentDTO.patientId());
    }
}
