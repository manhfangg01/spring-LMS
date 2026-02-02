package com.quiz.quizproject.domain.exam.dto.request;

import com.quiz.quizproject.util.constant.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExamRequest(
        @NotBlank(message = "Tiêu đề không được để trống")
        String title,
        @NotNull(message = "Loại bài thi không được để trống")
        ExamType examType,
        @NotNull(message = "Thời lượng không được để trống")
        Long durationInSeconds,
        @NotBlank(message = "Mô tả không được để trống")
        String description
)
{
}
