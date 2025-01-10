package com.github.ku4marez.appointmentnotifications.configuration;

import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import com.github.ku4marez.commonlibraries.entity.enums.NotificationStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfiguration {

    private final MongoTemplate mongoTemplate;

    public DatabaseConfiguration(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        initializeNotificationsCollection();
    }

    // =================== DOCTORS COLLECTION ===================
    private void initializeNotificationsCollection() {
        String collectionName = "notifications";

        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
        applyNotificationValidationRules(collectionName);
        seedDefaultNotifications();
    }

    private void applyNotificationValidationRules(String collectionName) {
        String validationSchema = """
        {
            "$jsonSchema": {
                "bsonType": "object",
                "required": ["content", "status", "creationDate", "updatedDate"],
                "properties": {
                    "id": { "bsonType": "string" },
                    "doctorId": { "bsonType": ["string", "null"] },
                    "patientId": { "bsonType": ["string", "null"] },
                    "content": { "bsonType": "string" },
                    "status": {
                        "bsonType": "string",
                        "enum": ["PENDING", "SENT", "FAILED"]
                    },
                    "createdBy": { "bsonType": "string" },
                    "updatedBy": { "bsonType": "string" },
                    "creationDate": { "bsonType": "date" },
                    "updatedDate": { "bsonType": "date" }
                }
            }
        }
        """;

        mongoTemplate.executeCommand("""
        {
            "collMod": "%s",
            "validator": %s,
            "validationLevel": "strict",
            "validationAction": "error"
        }
        """.formatted(collectionName, validationSchema));
    }

    private void seedDefaultNotifications() {
        if (mongoTemplate.findAll(NotificationEntity.class).isEmpty()) {
            NotificationEntity doctorNotification = new NotificationEntity();
            doctorNotification.setDoctorId("64a67b12e45c9c2ff91f5a3d");
            doctorNotification.setPatientId(null);
            doctorNotification.setStatus(NotificationStatus.PENDING);
            doctorNotification.setContent("A new appointment has been scheduled with patient: Jane Doe");
            mongoTemplate.save(doctorNotification);

            NotificationEntity patientNotification = new NotificationEntity();
            patientNotification.setDoctorId(null);
            patientNotification.setPatientId("64a67b12e45c9c2ff91f5a3e");
            patientNotification.setStatus(NotificationStatus.PENDING);
            patientNotification.setContent("A new appointment has been scheduled with Dr. John Smith");
            mongoTemplate.save(patientNotification);
        }
    }
}
