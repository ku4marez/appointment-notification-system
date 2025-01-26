package com.github.ku4marez.appointmentnotifications.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoDbConnectionTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testMongoTemplateConnection() {
        assertThat(mongoTemplate).isNotNull();

        String databaseName = mongoTemplate.getDb().getName();
        assertThat(databaseName).isEqualTo("test");
    }
}
