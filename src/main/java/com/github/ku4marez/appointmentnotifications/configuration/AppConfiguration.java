package com.github.ku4marez.appointmentnotifications.configuration;

import com.github.ku4marez.commonlibraries.util.AuditorAwareUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorProvider")
public class AppConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareUtil(this::getCurrentUser);
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }

        return "system";
    }
}
