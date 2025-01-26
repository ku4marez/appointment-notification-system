package com.github.ku4marez.appointmentnotifications.controller;

import com.github.ku4marez.appointmentnotifications.constant.TestConstants;
import com.github.ku4marez.appointmentnotifications.dto.NotificationDTO;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.appointmentnotifications.query.FindNotificationsByDoctorQuery;
import com.github.ku4marez.appointmentnotifications.util.CreateEntityUtil;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.github.ku4marez.appointmentnotifications.query.FindNotificationsByPatientQuery;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryGateway queryGateway;

    private NotificationDTO notificationDTO;

    @BeforeEach
    void setup() {
        notificationDTO = CreateEntityUtil.createDefaultNotificationDTO();
    }

    @Test
    void testGetNotificationsForDoctor() throws Exception {
        List<NotificationDTO> notifications = List.of(notificationDTO);

        when(queryGateway.query(any(FindNotificationsByDoctorQuery.class), eq(ResponseTypes.multipleInstancesOf(NotificationEntity.class))))
                .thenReturn(CompletableFuture.completedFuture(notifications.stream()
                        .map(CreateEntityUtil::createNotificationEntity)
                        .toList()));

        mockMvc.perform(get("/notifications/doctor/{doctorId}", TestConstants.DOCTOR_ID)
                        .param("page", "0")
                        .param("size", "10")
                        .with(SecurityMockMvcRequestPostProcessors.user("doctor").roles("DOCTOR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetNotificationsForPatient() throws Exception {
        List<NotificationDTO> notifications = List.of(notificationDTO);

        when(queryGateway.query(any(FindNotificationsByPatientQuery.class), eq(ResponseTypes.multipleInstancesOf(NotificationEntity.class))))
                .thenReturn(CompletableFuture.completedFuture(notifications.stream()
                        .map(CreateEntityUtil::createNotificationEntity)
                        .toList()));

        mockMvc.perform(get("/notifications/patient/{patientId}", TestConstants.PATIENT_ID)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
