package com.quiz.quizproject.domain.question.dto.response;

public record QuestionOptionResponse(
        Long id,
        String content,
        Boolean isCorrect,
        Long groupId) {
}
