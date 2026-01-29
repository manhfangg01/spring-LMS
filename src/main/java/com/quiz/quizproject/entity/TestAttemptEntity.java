package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.service.constant.TestAttemptStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_attempts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestAttemptEntity extends BaseEntity {
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    private Double score;
    @Enumerated(EnumType.STRING)
    private TestAttemptStatus status;
}
