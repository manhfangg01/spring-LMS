package com.quiz.quizproject.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionOptionRequest(
        @NotBlank(message = "Nội dung đáp án không được để trống")
        String content,
        @NotNull(message = "Trạng thái đúng/sai của đáp án không được để trống")
        Boolean isCorrect) {
}
