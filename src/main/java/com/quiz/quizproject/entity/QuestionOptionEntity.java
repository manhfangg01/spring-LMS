package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_options")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOptionEntity extends BaseEntity {
    private String content;
    private Boolean isCorrect;

    @ManyToOne()
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

}
