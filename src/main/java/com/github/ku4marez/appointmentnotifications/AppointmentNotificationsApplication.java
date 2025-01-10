package com.github.ku4marez.appointmentnotifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class AppointmentNotificationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentNotificationsApplication.class, args);
	}

}
