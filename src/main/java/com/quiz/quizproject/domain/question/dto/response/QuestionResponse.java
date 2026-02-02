package com.quiz.quizproject.domain.question.dto.response;

import com.quiz.quizproject.util.constant.QuestionType;
import java.util.List;

public record QuestionResponse(
        Long id,
        String content,
        QuestionType questionType,
        String correctText,
        String explanation,
        Integer orderIndex,
        Long materialId,
        List<QuestionOptionResponse> questionOptions) {
}
