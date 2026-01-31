package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.util.constant.ExamType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamEntity extends BaseEntity {
    private String title;
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    private Long durationInSeconds;
    private String totalQuestions;
    private String description;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamMaterialEntity> examMaterials;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestAttemptEntity> testAttempts;
}
