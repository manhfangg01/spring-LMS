package com.quiz.quizproject.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import com.quiz.quizproject.domain.userAnswer.UserAnswerEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "correct_answer", columnDefinition = "json")
    private List<String> correctAnswers; // for gap-fill

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
