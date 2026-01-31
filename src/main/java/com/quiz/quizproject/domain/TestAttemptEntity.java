package com.quiz.quizproject.domain;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.util.constant.TestAttemptStatus;
import jakarta.persistence.*;
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

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "exam_id")
    private  ExamEntity exam;

    @OneToMany(mappedBy = "testAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<UserAnswerEntity> userAnswers;



}
