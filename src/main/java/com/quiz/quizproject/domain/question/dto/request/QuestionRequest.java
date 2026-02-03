package com.quiz.quizproject.domain.question.dto.request;

import com.quiz.quizproject.util.constant.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionRequest(
        @NotBlank(message = "Nội dung câu hỏi không được để trống")
        String content,
        @NotNull(message = "Loại câu hỏi không được để trống")
        QuestionType questionType,
        String explanation,
        Integer orderIndex,
        Long materialId,
        List<QuestionOptionRequest> questionOptions) {
}
