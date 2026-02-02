package com.quiz.quizproject.domain;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_answers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswerEntity extends BaseEntity {
    private String inputText;
    private Boolean isCorrect;
    private Boolean isFlagged;

    @ManyToOne()
    @JoinColumn(name = "test_attempt_id")
    private TestAttemptEntity testAttempt;

    @ManyToOne()
    @JoinColumn(name = "question_id")
    private QuestionEntity question;
}
