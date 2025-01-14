package com.github.ku4marez.appointmentnotifications.handler;

import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.appointmentnotifications.query.FindNotificationsByDoctorQuery;
import com.github.ku4marez.appointmentnotifications.query.FindNotificationsByPatientQuery;
import com.github.ku4marez.appointmentnotifications.repository.NotificationRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationQueryHandler {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationQueryHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @QueryHandler
    public List<NotificationEntity> handle(FindNotificationsByDoctorQuery query) {
        return notificationRepository.findByDoctorId(query.doctorId());
    }

    @QueryHandler
    public List<NotificationEntity> handle(FindNotificationsByPatientQuery query) {
        return notificationRepository.findByPatientId(query.patientId());
    }
}
