package com.quiz.quizproject.domain.question.service;

import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
    QuestionResponse createQuestion(QuestionRequest request);

    QuestionResponse getQuestionById(Long id);

    Page<QuestionResponse> getAllQuestions(Pageable pageable);

    QuestionResponse updateQuestion(Long id, QuestionRequest request);

    void deleteQuestion(Long id);
}
