package com.quiz.quizproject.domain.questionGroup.dto.response;

import com.quiz.quizproject.util.constant.QuestionGroupType;

public record QuestionGroupResponse(
                Long id,
                String imageUrl,
                String instructions,
                Integer orderIndex,
                QuestionGroupType questionGroupType,
                Long partId) {
}
