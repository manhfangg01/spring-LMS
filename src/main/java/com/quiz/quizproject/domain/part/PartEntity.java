package com.quiz.quizproject.domain.part;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
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
    private ExamType examType;
    private String passage;
    private String audioUrl;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @OneToMany(mappedBy = "part")
    private List<QuestionGroupEntity> questionGroups;
}
