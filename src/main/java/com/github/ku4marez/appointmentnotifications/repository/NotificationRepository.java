package com.github.ku4marez.appointmentnotifications.repository;

import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByPatientId(String patientId);
    List<NotificationEntity> findByDoctorId(String doctorId);

}
