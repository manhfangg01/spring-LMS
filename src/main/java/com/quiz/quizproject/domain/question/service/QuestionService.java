package com.quiz.quizproject.domain.question.service;

import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {
    DetailedQuestionResponse createQuestion(Long groupId,QuestionRequest request);

    DetailedQuestionResponse getQuestionById(Long id);

    Page<DetailedQuestionResponse> getAllQuestions(Pageable pageable);

    DetailedQuestionResponse updateQuestion(Long id, QuestionRequest request);

    void deleteQuestion(Long id);

    void reorderQuestions(Long groupId, List<Long> orderedIds);

    void moveQuestionToAnotherGroup(Long questionId, Long targetGroupId);
}
