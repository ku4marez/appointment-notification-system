package com.github.ku4marez.appointmentnotifications.dto;

import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;

public record CreateNotificationCommand(String doctorId, String patientId, String content, NotificationStatus status) {}
