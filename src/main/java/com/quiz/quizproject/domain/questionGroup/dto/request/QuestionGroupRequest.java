package com.quiz.quizproject.domain.questionGroup.dto.request;

import com.quiz.quizproject.util.constant.QuestionType;

public record QuestionGroupRequest(
         Long id,
         String contentText,
         String audioUrl,
         String imageUrl,
         String instructions,
         QuestionType type
) {
}
