package com.quiz.quizproject.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record QuestionRequest(
        @NotBlank(message = "quesions content cannot be empty")
        String content,
        String explanation,
        List<QuestionOptionRequest> questionOptions,
        // dành riêng cho câu hỏi dạng điền khuyết
        List<String> correctAnswers){
}
