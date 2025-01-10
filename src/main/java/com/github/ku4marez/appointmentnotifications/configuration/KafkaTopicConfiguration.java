package com.github.ku4marez.appointmentnotifications.configuration;

import com.github.ku4marez.commonlibraries.constant.KafkaConstants;
import com.github.ku4marez.commonlibraries.util.KafkaTopicUtil;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KafkaTopicConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTopicUtil kafkaTopicManager() {
        return new KafkaTopicUtil(bootstrapServers);
    }

    @Bean
    public void setupTopics(KafkaTopicUtil kafkaTopicManager) {
        List<NewTopic> topics = List.of(
                new NewTopic(KafkaConstants.APPOINTMENT_CREATED_TOPIC, 3, (short) 1),
                new NewTopic(KafkaConstants.APPOINTMENT_UPDATED_TOPIC, 3, (short) 1),
                new NewTopic(KafkaConstants.NOTIFICATION_SENT_TOPIC, 3, (short) 1)
        );

        kafkaTopicManager.createTopics(topics);
    }
}

