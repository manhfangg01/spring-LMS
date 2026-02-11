package com.quiz.quizproject.domain.sharedOption.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedOptionResponse {
    private Long id;
    private String content;
    private String label;
    private String optionGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
