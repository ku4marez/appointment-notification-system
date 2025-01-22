package com.github.ku4marez.appointmentnotifications.listener;

import com.github.ku4marez.appointmentnotifications.dto.NotificationSentEvent;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.commonlibraries.dto.event.KafkaEvent;
import com.github.ku4marez.commonlibraries.util.KafkaProducerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.github.ku4marez.commonlibraries.constant.KafkaConstants.*;

@Component
@Slf4j
public class NotificationEventListener {
    private final KafkaProducerUtil kafkaProducerUtil;

    public NotificationEventListener(KafkaProducerUtil kafkaProducerUtil) {
        this.kafkaProducerUtil = kafkaProducerUtil;
    }

    @EventListener
    public void handleNotificationSent(NotificationSentEvent event) {
        NotificationEntity notification = event.getNotification();
        log.info("New notification sent: {}", notification.getId());
        publishNotificationSentEvent(notification.getId());
    }

    public void publishNotificationSentEvent(String notificationId) {
        KafkaEvent<String> event = new KafkaEvent<>(NOTIFICATION_SENT_TOPIC, notificationId);
        kafkaProducerUtil.sendMessage(NOTIFICATION_SENT_TOPIC, notificationId, event.toJson());
    }
}
