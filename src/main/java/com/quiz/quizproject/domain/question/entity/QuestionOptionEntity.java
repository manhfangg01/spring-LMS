package com.quiz.quizproject.domain.question.entity;

import com.quiz.quizproject.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_options")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOptionEntity extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;
}
