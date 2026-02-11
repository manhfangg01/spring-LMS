package com.quiz.quizproject.domain.question.dto.response;

import com.quiz.quizproject.domain.sharedOption.dto.response.SharedOptionResponse;
import com.quiz.quizproject.util.constant.QuestionGroupType;

import java.util.List;

public record DetailedQuestionResponse(
        Long id,
        String content,
        List<String> correctAnswers,
        String explanation,
        Integer orderIndex,
        QuestionGroupType type,
        List<QuestionOptionResponse> questionOptions,
        SharedOptionResponse sharedOption,
        Long groupId
) {
}
