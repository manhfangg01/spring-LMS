package com.quiz.quizproject.domain.exam.service;

import com.quiz.quizproject.domain.exam.dto.request.ExamRequest;
import com.quiz.quizproject.domain.exam.dto.response.ExamResponse;
import com.quiz.quizproject.domain.exam.filter.ExamFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamService {
    Page<ExamResponse> getExams(ExamFilter filter, Pageable pageable);

    ExamResponse getExamById(Long id);

    ExamResponse createExam(ExamRequest request);

    ExamResponse updateExam(Long id, ExamRequest request);

    void deleteExam(Long id);
}
