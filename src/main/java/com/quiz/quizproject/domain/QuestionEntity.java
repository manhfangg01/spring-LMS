package com.quiz.quizproject.domain;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.util.constant.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionEntity extends BaseEntity {
    private String content;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    //nếu là dạng FILL_IN_THE_BLANK thì sẽ lưu các đáp án theo dạng chuỗi ngăn cách nhau bởi dấu | trong trường hợp có nhiều đáp án đúng.
    private String correctText;
    private String explanation;
    private Integer orderIndex;

    @ManyToOne()
    @JoinColumn(name = "material_id")
    private MaterialEntity material;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOptionEntity> questionOptions;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAnswerEntity> userAnswers;


}
