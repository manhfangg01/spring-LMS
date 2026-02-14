package com.quiz.quizproject.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import com.quiz.quizproject.domain.sharedOption.SharedOptionEntity;
import com.quiz.quizproject.domain.userAnswer.UserAnswerEntity;
import com.quiz.quizproject.util.constant.QuestionGroupType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEntity extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    private QuestionGroupType type;// cột này để xác định có cần correct_shared_option_id hay không

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "correct_answer", columnDefinition = "json")
    private List<String> correctAnswers; // for gap-fill


    @ManyToOne()
    @JoinColumn(name = "question_group_id")
    private QuestionGroupEntity questionGroup;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOptionEntity> questionOptions = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserAnswerEntity> userAnswers;

    @OneToOne
    @JoinColumn(name = "correct_shared_option_id", nullable = true)
    private SharedOptionEntity correctSharedOption;

//    public void addOption(QuestionOptionEntity option) {
//        questionOptions.add(option);
//        option.setQuestion(this);
//    }  // chỉ cần dùng mapper không cần thằng này nữa
}
