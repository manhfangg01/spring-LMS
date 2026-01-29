package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.service.constant.QuestionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String correctText;     //nếu là dạng FILL_IN_THE_BLANK thì sẽ lưu các đáp án theo dạng chuỗi ngăn cách nhau bởi dấu | trong trường hợp có nhiều đáp án đúng.
    private String explanation;
    private Integer orderIndex;
}
