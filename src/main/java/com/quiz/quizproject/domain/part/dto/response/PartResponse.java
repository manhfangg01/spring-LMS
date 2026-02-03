package com.quiz.quizproject.domain.part.dto.response;

import com.quiz.quizproject.util.constant.ExamType;

public record PartResponse(
                Long id,
                String title,
                Integer orderIndex,
                ExamType examType,
                String passage,
                String audioUrl,
                String description,
                Long examId) {
}
