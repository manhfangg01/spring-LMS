package com.quiz.quizproject.domain.part.dto.request;

import com.quiz.quizproject.util.constant.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PartRequest(
                @NotBlank(message = "Title cannot be empty") String title,
                @NotNull(message = "Order index is required") Integer orderIndex,
                ExamType examType,
                String passage,
                String audioUrl,
                String description) {
}
