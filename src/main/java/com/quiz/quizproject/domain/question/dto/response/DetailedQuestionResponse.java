package com.quiz.quizproject.domain.question.dto.response;
import java.util.List;

public record DetailedQuestionResponse(
        Long id,
        String content,
        List<String> correctAnswers,
        String explanation,
        Integer orderIndex,
        List<QuestionOptionResponse> questionOptions,
        Long groupId
) {
}
