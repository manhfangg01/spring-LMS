package com.quiz.quizproject.domain.questionGroup;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import com.quiz.quizproject.domain.sharedOption.SharedOptionEntity;
import com.quiz.quizproject.util.constant.QuestionGroupType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "question_groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionGroupEntity extends BaseEntity {
    private String imageUrl;
    private String instructions;
    private Integer orderIndex;
    @Enumerated(EnumType.STRING)
    private QuestionGroupType type;


    @ManyToOne
    @JoinColumn(name = "part_id")
    private PartEntity part;

    @OneToMany(mappedBy = "questionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedOptionEntity> sharedOptions;



}
