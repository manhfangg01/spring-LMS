package com.quiz.quizproject.domain.questionGroup.dto.response;

import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import com.quiz.quizproject.domain.sharedOption.dto.response.SharedOptionResponse;
import com.quiz.quizproject.util.constant.QuestionGroupType;

import java.util.List;

public record DetailedQuestionGroupResponse(
        Long id,
        String imageUrl,
        String instructions,
        Integer orderIndex,
        QuestionGroupType type,
        List<SharedOptionResponse> sharedOptions,
        List<DetailedQuestionResponse> questions,
        Long partId
) {
}
