package com.quiz.quizproject.domain;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "materials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialEntity extends BaseEntity {
    private String title;
    private String contentText;
    private String audioUrl;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamMaterialEntity> examMaterials;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions;

}
