package com.quiz.quizproject.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.QuestionGroupEntity;
import com.quiz.quizproject.domain.UserAnswerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionEntity extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private Integer orderIndex;

    @ManyToOne()
    @JoinColumn(name = "question_group_id")
    private QuestionGroupEntity questionGroup;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOptionEntity> questionOptions = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserAnswerEntity> userAnswers;

    public void addOption(QuestionOptionEntity option) {
        questionOptions.add(option);
        option.setQuestion(this);
    }
}
