package com.quiz.quizproject.domain.question.service;

import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
    DetailedQuestionResponse createQuestion(QuestionRequest request);

    DetailedQuestionResponse getQuestionById(Long id);

    Page<DetailedQuestionResponse> getAllQuestions(Pageable pageable);

    DetailedQuestionResponse updateQuestion(Long id, QuestionRequest request);

    void deleteQuestion(Long id);
}
