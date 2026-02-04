package com.quiz.quizproject.domain.exam.dto.response;

import com.quiz.quizproject.util.constant.ExamStatus;

public record ExamResponse(
        Long id,
        String title,
        String code,
        String examType,
        Long durationInSeconds,
        String description,
        String totalQuestions,
        ExamStatus examStatus
) {
}
