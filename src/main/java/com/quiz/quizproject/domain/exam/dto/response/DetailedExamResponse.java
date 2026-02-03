package com.quiz.quizproject.domain.exam.dto.response;


public record DetailedExamResponse(
        Long id,
        String title,
        String code,
        String examType,
        Long durationInSeconds,
        String description,
        Integer totalQuestions
) {
}
