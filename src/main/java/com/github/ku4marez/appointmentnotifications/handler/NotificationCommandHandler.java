package com.github.ku4marez.appointmentnotifications.handler;

import com.github.ku4marez.appointmentnotifications.dto.CreateNotificationCommand;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.appointmentnotifications.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationCommandHandler {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationCommandHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @CommandHandler
    public void handle(CreateNotificationCommand command) {
        NotificationEntity notification = new NotificationEntity();
        notification.setDoctorId(command.doctorId());
        notification.setPatientId(command.patientId());
        notification.setContent(command.content());
        notification.setStatus(command.status());
        notificationRepository.save(notification);

        log.info("Notification created: {}", notification);
    }
}
