package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.service.constant.ExamType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamEntity extends BaseEntity {
    private String title;
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    private Long durationInSeconds;
    private String totalQuestions;
    private String description;
}
