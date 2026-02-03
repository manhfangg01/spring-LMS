package com.quiz.quizproject.domain.questionGroup.dto.response;

import com.quiz.quizproject.util.constant.QuestionType;

public record QuestionGroupResponse(
        Long id,
        String title,
        String contentText,
        String audioUrl,
        String imageUrl,
        String instructions,
        QuestionType type
) {
}
