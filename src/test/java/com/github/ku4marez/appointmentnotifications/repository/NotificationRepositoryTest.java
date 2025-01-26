package com.github.ku4marez.appointmentnotifications.repository;

import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.appointmentnotifications.util.CreateEntityUtil;
import com.github.ku4marez.appointmentnotifications.constant.TestConstants;
import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    public void setUp() {
        notificationRepository.deleteAll();
    }

    @Test
    void testFindByPatientId() {
        NotificationEntity notification1 = CreateEntityUtil.createNotificationEntity(
                null, TestConstants.PATIENT_ID, "Patient notification content", NotificationStatus.PENDING);
        NotificationEntity notification2 = CreateEntityUtil.createNotificationEntity(
                null, TestConstants.PATIENT_ID, "Another patient notification", NotificationStatus.PENDING);
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        List<NotificationEntity> results = notificationRepository.findByPatientId(TestConstants.PATIENT_ID);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(TestConstants.PATIENT_ID, results.getFirst().getPatientId());
    }

    @Test
    void testFindByDoctorId() {
        NotificationEntity notification1 = CreateEntityUtil.createNotificationEntity(
                TestConstants.DOCTOR_ID, null, "Doctor notification content", NotificationStatus.PENDING);
        notificationRepository.save(notification1);

        List<NotificationEntity> results = notificationRepository.findByDoctorId(TestConstants.DOCTOR_ID);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(TestConstants.DOCTOR_ID, results.getFirst().getDoctorId());
    }
}
