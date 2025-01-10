package com.github.ku4marez.appointmentnotifications.controller;

import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.appointmentnotifications.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/doctor/{doctorId}")
    public List<NotificationEntity> getNotificationsForDoctor(@PathVariable String doctorId) {
        return notificationRepository.findByDoctorId(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<NotificationEntity> getNotificationsForPatient(@PathVariable String patientId) {
        return notificationRepository.findByPatientId(patientId);
    }
}
