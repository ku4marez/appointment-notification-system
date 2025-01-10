package com.github.ku4marez.appointmentnotifications.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notifications")
public class NotificationEntity extends PersistentAuditedEntity {

    private String doctorId;
    private String patientId;

    private String content;
    private NotificationStatus status;

}
