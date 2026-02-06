package com.quiz.quizproject.domain.questionGroup.dto.response;

import com.quiz.quizproject.util.constant.QuestionType;

public record QuestionGroupResponse(
                Long id,
                String imageUrl,
                String instructions,
                QuestionType type,
                Integer orderIndex,
                Long partId) {
}
