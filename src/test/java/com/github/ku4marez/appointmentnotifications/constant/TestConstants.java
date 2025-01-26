package com.github.ku4marez.appointmentnotifications.constant;

import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestConstants {

    private TestConstants() {
    }

    public static final String CONTENT_DOCTOR = "A new appointment has been scheduled.";
    public static final String CONTENT_PATIENT = "Your appointment has been scheduled.";
    public static final NotificationStatus STATUS = NotificationStatus.PENDING;

    // Doctor Constants
    public static final String DOCTOR_ID = "1";
    public static final String DOCTOR_FIRST_NAME = "John";
    public static final String DOCTOR_LAST_NAME = "Smith";
    public static final String DOCTOR_EMAIL = "john.smith@clinic.com";
    public static final String DOCTOR_PHONE = "123-456-7890";
    public static final String DOCTOR_SPECIALTY = "Cardiology";
    public static final String DOCTOR_LICENSE = "DOC123456";

    // Patient Constants
    public static final String PATIENT_ID = "1";
    public static final String PATIENT_FIRST_NAME = "Jane";
    public static final String PATIENT_LAST_NAME = "Doe";
    public static final String PATIENT_EMAIL = "jane.doe@clinic.com";
    public static final LocalDate PATIENT_DOB = LocalDate.of(1990, 1, 1);
    public static final String PATIENT_PHONE = "987-654-3210";
    public static final String PATIENT_ADDRESS = "123 Main St, Springfield";
    public static final String PATIENT_RECORD_NUMBER = "PAT123456";

    // Appointment Constants
    public static final String APPOINTMENT_ID = "1";
    public static final LocalDateTime APPOINTMENT_DATETIME = LocalDateTime.now().plusHours(1);
    public static final String APPOINTMENT_STATUS = "Scheduled";
    public static final String APPOINTMENT_REASON = "Routine Checkup";
}
