package com.github.ku4marez.appointmentnotifications.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ku4marez.appointmentnotifications.service.NotificationService;
import com.github.ku4marez.commonlibraries.constant.KafkaConstants;
import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.event.KafkaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentEventListener {

    private final NotificationService notificationService;

    @Autowired
    public AppointmentEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = KafkaConstants.APPOINTMENT_CREATED_TOPIC, groupId = "notification-service-group")
    public void handleAppointmentCreated(String message) {
        log.info("Received appointment.created event: {}", message);
        try {
            KafkaEvent<AppointmentDTO> event = new ObjectMapper().readValue(message, KafkaEvent.class);
            AppointmentDTO appointment = event.getPayload();

            // Create notifications for doctor and patient
            notificationService.createNotificationForAppointment(
                    appointment
            );
        } catch (Exception e) {
            log.error("Error processing appointment.created event: {}", e.getMessage(), e);
        }
    }
}
