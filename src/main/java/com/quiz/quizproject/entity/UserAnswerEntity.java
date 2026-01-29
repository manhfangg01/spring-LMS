package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_answers")
public class UserAnswerEntity extends BaseEntity {
    private String inputText;
    private Boolean isCorrect;
    private Boolean isFlagged;
}
