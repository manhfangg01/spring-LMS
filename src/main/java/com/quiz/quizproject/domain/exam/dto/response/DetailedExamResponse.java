package com.quiz.quizproject.domain.exam.dto.response;


import com.quiz.quizproject.domain.part.dto.response.DetailedPartResponse;
import com.quiz.quizproject.util.constant.ExamStatus;

import java.util.List;

public record DetailedExamResponse(
        Long id,
        String title,
        String code,
        String examType,
        Long durationInSeconds,
        String description,
        ExamStatus examStatus,
        List<DetailedPartResponse> parts
) {
}
