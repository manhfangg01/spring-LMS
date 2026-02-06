package com.quiz.quizproject.domain.part.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PartRequest(
                @NotBlank(message = "Title cannot be empty")
                String title,
                String passage,
                String audioUrl,
                String description) {
}
