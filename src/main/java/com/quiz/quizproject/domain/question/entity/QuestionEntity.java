package com.quiz.quizproject.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.MaterialEntity;
import com.quiz.quizproject.domain.UserAnswerEntity;
import com.quiz.quizproject.util.constant.QuestionType;
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

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private Integer orderIndex;

    private String instructions;

    @ManyToOne()
    @JoinColumn(name = "material_id")
    private MaterialEntity material;

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
