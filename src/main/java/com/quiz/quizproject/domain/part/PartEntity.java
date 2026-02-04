package com.quiz.quizproject.domain.part;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.questionGroup.entity.QuestionGroupEntity;
import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.util.constant.ExamType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="parts")
@Getter
@Setter
public class PartEntity extends BaseEntity {
    private String title;
    private Integer orderIndex;
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    private String passage;
    private String audioUrl;
    private String description;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = true)
    private ExamEntity exam;

    @OneToMany(mappedBy = "part")
    private List<QuestionGroupEntity> questionGroups;
}
