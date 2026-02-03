package com.quiz.quizproject.domain.exam.dto.request;

import com.quiz.quizproject.util.constant.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExamRequest(
                @NotBlank(message = "Title cannot be empty") String title,
                @NotNull(message = "Exam type cannot be null") ExamType examType,
                @NotNull(message = "Duration cannot be null") Long durationInSeconds,
                @NotBlank(message = "Description cannot be empty") String description,
                @NotBlank(message = "Exam code cannot be empty") String code) {
}
