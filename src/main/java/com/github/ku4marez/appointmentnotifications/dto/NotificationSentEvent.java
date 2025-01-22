package com.github.ku4marez.appointmentnotifications.dto;

import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationSentEvent extends ApplicationEvent {
    private final NotificationEntity notification;

    public NotificationSentEvent(Object source, NotificationEntity notification) {
        super(source);
        this.notification = notification;
    }
}
