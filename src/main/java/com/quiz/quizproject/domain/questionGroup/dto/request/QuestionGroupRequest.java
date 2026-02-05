package com.quiz.quizproject.domain.questionGroup.dto.request;

import com.quiz.quizproject.util.constant.QuestionType;
import jakarta.validation.constraints.NotNull;

public record QuestionGroupRequest(
        String imageUrl,
        String instructions,
        @NotNull(message = "questionGroup type cannot be null")
        QuestionType type
   ) {
}
