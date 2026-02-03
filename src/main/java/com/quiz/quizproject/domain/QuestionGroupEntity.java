package com.quiz.quizproject.domain;

import com.quiz.quizproject.base.BaseEntity;
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
    // This is equal to QuestionGroup
    private String title;
    private String contentText;
    private String audioUrl;
    private String imageUrl;
    private String instructions;
    @Enumerated(EnumType.STRING)
    private QuestionType type;


    @ManyToOne
    @JoinColumn(name = "part_id")
    private PartEntity part;

    @OneToMany(mappedBy = "questionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions;
}
