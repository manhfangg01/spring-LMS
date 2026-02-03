package com.quiz.quizproject.domain.exam;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.testAttempt.TestAttemptEntity;
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
    @Column(unique = true)
    private String title;
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    private Long durationInSeconds;
    private Integer totalQuestions;
    private String description;
    @Column(unique = true)
    private String code; // for searching-purposes

    @OneToMany(mappedBy = "exam", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PartEntity> parts;// soft delete với parts

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestAttemptEntity> testAttempts; // hard delete với attempts
}
