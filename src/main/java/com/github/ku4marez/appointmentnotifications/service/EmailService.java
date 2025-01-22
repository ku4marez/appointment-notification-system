package com.github.ku4marez.appointmentnotifications.service;

import com.github.ku4marez.commonlibraries.dto.AppointmentDTO;
import com.github.ku4marez.commonlibraries.dto.DoctorDTO;
import com.github.ku4marez.commonlibraries.dto.PatientDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendAppointmentEmailToDoctor(DoctorDTO doctor, PatientDTO patient, AppointmentDTO appointment) {
        String subject = "New Appointment Scheduled";
        Context context = new Context();
        context.setVariable("doctorName", doctor.firstName() + " " + doctor.lastName());
        context.setVariable("patientName", patient.firstName() + " " + patient.lastName());
        context.setVariable("appointmentDateTime", appointment.dateTime());

        String body = templateEngine.process("email/appointment-doctor", context);
        sendEmail(doctor.email(), subject, body);
    }

    public void sendAppointmentEmailToPatient(DoctorDTO doctor, PatientDTO patient, AppointmentDTO appointment) {
        String subject = "Appointment Confirmation";
        Context context = new Context();
        context.setVariable("doctorName", doctor.firstName() + " " + doctor.lastName());
        context.setVariable("patientName", patient.firstName() + " " + patient.lastName());
        context.setVariable("appointmentDateTime", appointment.dateTime());

        String body = templateEngine.process("email/appointment-patient", context);
        sendEmail(patient.email(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Email sent to {} with subject: {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
