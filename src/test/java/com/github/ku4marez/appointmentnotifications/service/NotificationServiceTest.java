package com.github.ku4marez.appointmentnotifications.service;

import com.github.ku4marez.appointmentnotifications.constant.TestConstants;
import com.github.ku4marez.appointmentnotifications.dto.CreateNotificationCommand;
import com.github.ku4marez.appointmentnotifications.dto.NotificationSentEvent;
import com.github.ku4marez.appointmentnotifications.util.CreateEntityUtil;
import com.github.ku4marez.appointmentnotifications.endpoint.ClinicManagementClient;
import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private ClinicManagementClient clinicManagementClient;

    @Mock
    private CommandGateway commandGateway;

    @Mock
    private EmailService emailService;

    @Mock
    private SMSService smsService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testCreateNotificationForAppointment() {
        DoctorDTO mockDoctor = CreateEntityUtil.createDefaultDoctorDTO();
        PatientDTO mockPatient = CreateEntityUtil.createDefaultPatientDTO();
        AppointmentDTO appointmentDTO = CreateEntityUtil.createDefaultAppointmentDTO();

        when(clinicManagementClient.getDoctorById(TestConstants.DOCTOR_ID)).thenReturn(mockDoctor);
        when(clinicManagementClient.getPatientById(TestConstants.PATIENT_ID)).thenReturn(mockPatient);

        notificationService.createNotificationForAppointment(appointmentDTO);

        verify(commandGateway, times(2)).sendAndWait(any(CreateNotificationCommand.class));
        verify(emailService).sendAppointmentEmailToDoctor(mockDoctor, mockPatient, appointmentDTO);
        verify(emailService).sendAppointmentEmailToPatient(mockDoctor, mockPatient, appointmentDTO);
        verify(smsService).sendAppointmentSMSToDoctor(mockDoctor, mockPatient, appointmentDTO);
        verify(smsService).sendAppointmentSMSToPatient(mockDoctor, mockPatient, appointmentDTO);
        verify(eventPublisher, times(2)).publishEvent(any(NotificationSentEvent.class));
    }
}
