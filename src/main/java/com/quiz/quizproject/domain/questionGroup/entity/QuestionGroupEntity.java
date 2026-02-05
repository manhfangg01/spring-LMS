package com.quiz.quizproject.domain.questionGroup.entity;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import com.quiz.quizproject.util.constant.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "question_groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGroupEntity extends BaseEntity {
    private String imageUrl;
    private String instructions;
    private Integer orderIndex;
    @Enumerated(EnumType.STRING)
    private QuestionType type;


    @ManyToOne
    @JoinColumn(name = "part_id")
    private PartEntity part;

    @OneToMany(mappedBy = "questionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions;
}
