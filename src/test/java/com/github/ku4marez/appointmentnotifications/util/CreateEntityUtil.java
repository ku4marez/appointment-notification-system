package com.github.ku4marez.appointmentnotifications.util;

import com.github.ku4marez.appointmentnotifications.dto.NotificationDTO;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;

import java.time.LocalDateTime;

import static com.github.ku4marez.appointmentnotifications.constant.TestConstants.*;

public class CreateEntityUtil {

    private CreateEntityUtil() {
    }

    // Create NotificationEntity
    public static NotificationEntity createNotificationEntity(String doctorId, String patientId, String content, NotificationStatus status) {
        NotificationEntity notification = new NotificationEntity();
        notification.setDoctorId(doctorId);
        notification.setPatientId(patientId);
        notification.setContent(content);
        notification.setStatus(status);
        notification.setCreationDate(LocalDateTime.now());
        notification.setUpdatedDate(LocalDateTime.now());
        return notification;
    }

    public static NotificationEntity createNotificationEntity(NotificationDTO dto) {
        NotificationEntity notification = new NotificationEntity();
        notification.setDoctorId(dto.getDoctorId());
        notification.setPatientId(dto.getPatientId());
        notification.setContent(dto.getContent());
        notification.setStatus(dto.getStatus());
        notification.setCreationDate(LocalDateTime.now());
        notification.setUpdatedDate(LocalDateTime.now());
        return notification;
    }

    // Default NotificationEntity for Doctor
    public static NotificationEntity createDefaultDoctorNotification() {
        return createNotificationEntity(DOCTOR_ID, null, CONTENT_DOCTOR, STATUS);
    }

    // Default NotificationEntity for Patient
    public static NotificationEntity createDefaultPatientNotification() {
        return createNotificationEntity(null, PATIENT_ID, CONTENT_PATIENT, STATUS);
    }

    // Create NotificationDTO
    public static NotificationDTO createNotificationDTO(String doctorId, String patientId, String content, NotificationStatus status) {
        return NotificationDTO.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .content(content)
                .status(status)
                .build();
    }

    // Default NotificationDTO
    public static NotificationDTO createDefaultNotificationDTO() {
        return createNotificationDTO(DOCTOR_ID, PATIENT_ID, CONTENT_PATIENT, STATUS);
    }

    public static AppointmentDTO createDefaultAppointmentDTO() {
        return AppointmentDTO.builder()
                .reason(APPOINTMENT_REASON)
                .status(APPOINTMENT_STATUS)
                .dateTime(APPOINTMENT_DATETIME)
                .patientId(PATIENT_ID)
                .doctorId(DOCTOR_ID)
                .build();
    }

    public static DoctorDTO createDefaultDoctorDTO() {
        return DoctorDTO.builder()
                .firstName(DOCTOR_FIRST_NAME)
                .lastName(DOCTOR_LAST_NAME)
                .email(DOCTOR_EMAIL)
                .phoneNumber(DOCTOR_PHONE)
                .specialty(DOCTOR_SPECIALTY)
                .licenseNumber(DOCTOR_LICENSE)
                .build();
    }

    public static PatientDTO createDefaultPatientDTO() {
        return PatientDTO.builder()
                .firstName(PATIENT_FIRST_NAME)
                .lastName(PATIENT_LAST_NAME)
                .email(PATIENT_EMAIL)
                .dateOfBirth(PATIENT_DOB)
                .phoneNumber(PATIENT_PHONE)
                .address(PATIENT_ADDRESS)
                .medicalRecordNumber(PATIENT_RECORD_NUMBER)
                .build();
    }
}

