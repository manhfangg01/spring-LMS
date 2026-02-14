package com.quiz.quizproject.domain.part;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.util.constant.ExamType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="parts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartEntity extends BaseEntity {
    private String title;
    private Integer orderIndex;
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    private String passage;
    private String audioUrl;
    private String description;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionGroupEntity> questionGroups;
}
