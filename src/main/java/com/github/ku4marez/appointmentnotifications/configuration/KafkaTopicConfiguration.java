package com.github.ku4marez.appointmentnotifications.configuration;

import com.github.ku4marez.commonlibraries.util.KafkaTopicUtil;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

import static com.github.ku4marez.commonlibraries.constant.KafkaConstants.APPOINTMENT_CREATED_TOPIC;
import static com.github.ku4marez.commonlibraries.constant.KafkaConstants.APPOINTMENT_UPDATED_TOPIC;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaTopicConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTopicUtil kafkaTopicManager() {
        return new KafkaTopicUtil(bootstrapServers);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setupTopics() {
        KafkaTopicUtil kafkaTopicManager = kafkaTopicManager();
        List<NewTopic> topics = List.of(
                new NewTopic(APPOINTMENT_CREATED_TOPIC, 3, (short) 1),
                new NewTopic(APPOINTMENT_UPDATED_TOPIC, 3, (short) 1)
        );

        kafkaTopicManager.createTopics(topics);
    }
}

