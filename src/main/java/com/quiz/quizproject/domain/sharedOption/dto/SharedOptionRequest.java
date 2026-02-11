package com.quiz.quizproject.domain.sharedOption.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedOptionRequest {
    @NotBlank(message = "Content is required")
    private String content;
    
    private String label;
    
    private String optionGroup;
}
