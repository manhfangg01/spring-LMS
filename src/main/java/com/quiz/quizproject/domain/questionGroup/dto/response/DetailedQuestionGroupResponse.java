package com.quiz.quizproject.domain.questionGroup.dto.response;

import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import com.quiz.quizproject.util.constant.QuestionType;

import java.util.List;

public record DetailedQuestionGroupResponse(
        Long id,
        String title,
        String contentText,
        String audioUrl,
        String imageUrl,
        String instructions,
        QuestionType type,
        List<DetailedQuestionResponse> questions
) {
}
