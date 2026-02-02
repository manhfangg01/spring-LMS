package com.quiz.quizproject.domain.exam.dto.response;

public record ExamResponse(
        Long id,
        String title,
        String examType,
        Long durationInSeconds,
        String description
) {
}
