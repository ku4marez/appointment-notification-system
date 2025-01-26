package com.github.ku4marez.appointmentnotifications.mapper;

import com.github.ku4marez.appointmentnotifications.dto.NotificationDTO;
import com.github.ku4marez.appointmentnotifications.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDTO toDto(NotificationEntity notificationEntity);

    NotificationEntity toEntity(NotificationDTO notificationDTO);

    void updateEntityFromDto(NotificationDTO notificationDTO, @MappingTarget NotificationEntity notificationEntity);
}
