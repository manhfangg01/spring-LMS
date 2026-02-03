package com.quiz.quizproject.domain.part.dto.response;

import com.quiz.quizproject.domain.questionGroup.dto.response.DetailedQuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import com.quiz.quizproject.util.constant.ExamType;

import java.util.List;

public record DetailedPartResponse(
        Long id,
        String title,
        Integer orderIndex,
        ExamType examType,
        String passage,
        String audioUrl,
        List<DetailedQuestionGroupResponse> questionGroups
) {
}
