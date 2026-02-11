package com.quiz.quizproject.domain.sharedOption.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedOptionRequest {
    private String content;
    private String label;
    private String optionGroup;
}
