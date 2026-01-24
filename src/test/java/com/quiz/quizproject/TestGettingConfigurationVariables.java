package com.quiz.quizproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestGettingConfigurationVariables {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Test
    void load() {
        System.out.println("Database URL: " + dbUrl);
    }

}
