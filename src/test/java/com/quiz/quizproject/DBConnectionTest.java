package com.quiz.quizproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DBConnectionTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testConnection() {
        // Chạy một câu lệnh SQL đơn giản nhất
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        // Nếu kết quả trả về là 1, nghĩa là kết nối thành công
        assertThat(result).isEqualTo(1);
        System.out.println("--- Kết nối Database thành công! ---");
    }

}
