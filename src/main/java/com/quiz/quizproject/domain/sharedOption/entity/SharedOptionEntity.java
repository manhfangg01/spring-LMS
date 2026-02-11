package com.quiz.quizproject.domain.sharedOption.entity;

import com.quiz.quizproject.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shared_options")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedOptionEntity extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String label; // for options like A, B, C, D
    
    @Column(name = "option_group")
    private String optionGroup; // to group related options together
}
