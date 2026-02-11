package com.quiz.quizproject.domain.questionGroup.dto.request;

import com.quiz.quizproject.domain.sharedOption.SharedOptionEntity;
import com.quiz.quizproject.util.constant.QuestionGroupType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuestionGroupRequest(
        String imageUrl,
        @NotBlank(message = "Instructions cannot be empty")
        String instructions,
        @NotNull(message = "Order index cannot be null")
        QuestionGroupType type,
        @Size(max = 10, message = "Shared options cannot exceed 10")
        List<SharedOptionEntity> sharedOptions
   ) {
}
