package com.quiz.quizproject.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record QuestionRequest(
        @NotBlank(message = "quesions content cannot be empty")
        String content,
        String explanation,
        // Dành riêng cho các câu hỏi MATCHING dùng label để quyết định nên trỏ vào sharedOtption nào
        String labelMatching,
        List<QuestionOptionRequest> questionOptions,
        // dành riêng cho câu hỏi dạng điền khuyết
        List<String> correctAnswers
        // Về type thì sẽ do group nó thuộc về quyết định
){
}
