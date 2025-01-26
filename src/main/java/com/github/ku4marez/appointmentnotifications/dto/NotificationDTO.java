package com.github.ku4marez.appointmentnotifications.dto;

import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String doctorId;
    private String patientId;
    private String content;
    private NotificationStatus status;
}

