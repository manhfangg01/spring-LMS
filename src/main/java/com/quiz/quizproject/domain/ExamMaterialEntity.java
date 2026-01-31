package com.quiz.quizproject.domain;

import com.quiz.quizproject.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_materials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamMaterialEntity extends BaseEntity {
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private  ExamEntity exam;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private  MaterialEntity material;

}
