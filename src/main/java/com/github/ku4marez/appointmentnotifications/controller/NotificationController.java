package com.github.ku4marez.appointmentnotifications.controller;

import com.github.ku4marez.appointmentnotifications.dto.NotificationDTO;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.appointmentnotifications.mapper.NotificationMapper;
import com.github.ku4marez.appointmentnotifications.query.FindNotificationsByDoctorQuery;
import com.github.ku4marez.appointmentnotifications.query.FindNotificationsByPatientQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final QueryGateway queryGateway;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationController(QueryGateway queryGateway, NotificationMapper notificationMapper) {
        this.queryGateway = queryGateway;
        this.notificationMapper = notificationMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/doctor/{doctorId}")
    public List<NotificationDTO> getNotificationsForDoctor(@PathVariable String doctorId) {
        FindNotificationsByDoctorQuery query = new FindNotificationsByDoctorQuery(doctorId);
        List<NotificationEntity> notificationEntities = queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(NotificationEntity.class))
                .join();
        return notificationEntities.stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/patient/{patientId}")
    public List<NotificationDTO> getNotificationsForPatient(@PathVariable String patientId) {
        FindNotificationsByPatientQuery query = new FindNotificationsByPatientQuery(patientId);
        List<NotificationEntity> notificationEntities = queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(NotificationEntity.class))
                .join();
        return notificationEntities.stream()
                .map(notificationMapper::toDto)
                .toList();
    }
}

