package com.quiz.quizproject.domain.part.dto.response;

import com.quiz.quizproject.domain.questionGroup.dto.response.DetailedQuestionGroupResponse;
import com.quiz.quizproject.util.constant.ExamType;

import java.util.List;

public record DetailedPartResponse(
                Long id,
                String title,
                Integer orderIndex,
                ExamType examType,
                String passage,
                String audioUrl,
                String description,
                List<DetailedQuestionGroupResponse> questionGroups,
                Long examId) {// Exam chỉ không null khi nó được gọi bởi PartService
}
