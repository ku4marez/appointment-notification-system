package com.github.ku4marez.appointmentnotifications.configuration;

import com.github.ku4marez.commonlibraries.constant.KafkaConstants;
import com.github.ku4marez.commonlibraries.util.KafkaConsumerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public KafkaConsumerUtil kafkaConsumerUtil() {
        return new KafkaConsumerUtil(bootstrapServers, groupId, KafkaConstants.APPOINTMENT_CREATED_TOPIC);
    }
}
