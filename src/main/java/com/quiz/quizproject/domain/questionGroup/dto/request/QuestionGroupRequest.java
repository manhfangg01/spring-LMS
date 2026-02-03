package com.quiz.quizproject.domain.questionGroup.dto.request;

import com.quiz.quizproject.util.constant.QuestionType;
import jakarta.validation.constraints.NotNull;

public record QuestionGroupRequest(
        String imageUrl,
        String instructions,
        QuestionType type,
        @NotNull(message = "Part ID is required") Long partId) {
}
