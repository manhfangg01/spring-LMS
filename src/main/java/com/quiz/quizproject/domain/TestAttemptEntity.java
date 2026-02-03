package com.quiz.quizproject.domain;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.util.constant.TestAttemptStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "test_attempts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestAttemptEntity extends BaseEntity {
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    @Enumerated(EnumType.STRING)
    private TestAttemptStatus status;

    @Column(name = "total_correct")
    private Integer totalCorrect = 0;

    @Column(name = "total_incorrect")
    private Integer totalIncorrect = 0;

    @Column(name = "total_skipped")
    private Integer totalSkipped = 0;

    @Column(name = "total_score")
    private Double totalScore = 0.0;  // based on IELTS band

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skill_breakdown", columnDefinition = "json")
    private Map<String, Object> skillBreakdown;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @OneToMany(mappedBy = "testAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<UserAnswerEntity> userAnswers;



}
