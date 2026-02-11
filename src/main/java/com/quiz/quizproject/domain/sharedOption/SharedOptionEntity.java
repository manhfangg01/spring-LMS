package com.quiz.quizproject.domain.sharedOption;

import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shared_options")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedOptionEntity extends BaseEntity {
    // Nhãn hiển thị: "i", "ii", "A", "B"... (Dùng để FE render label)
    private String label;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private QuestionGroupEntity group;
}
