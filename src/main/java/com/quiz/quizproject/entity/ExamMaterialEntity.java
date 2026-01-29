package com.quiz.quizproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "exam_materials")
public class ExamMaterialEntity extends ExamEntity {
    private Integer orderIndex;
}
