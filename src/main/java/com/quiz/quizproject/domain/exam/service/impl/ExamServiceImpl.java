package com.quiz.quizproject.domain.exam.service.impl;

import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.domain.exam.dto.request.ExamRequest;
import com.quiz.quizproject.domain.exam.dto.response.DetailedExamResponse;
import com.quiz.quizproject.domain.exam.dto.response.ExamResponse;
import com.quiz.quizproject.domain.exam.filter.ExamFilter;
import com.quiz.quizproject.domain.exam.mapper.ExamMapper;
import com.quiz.quizproject.domain.exam.repository.ExamRepository;
import com.quiz.quizproject.domain.exam.service.ExamService;
import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.repository.PartRepository;
import com.quiz.quizproject.util.constant.ExamStatus;
import com.quiz.quizproject.util.exception.handler.AppException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final PartRepository partRepository;

    @Override
    public Page<ExamResponse> getExams(ExamFilter filter, Pageable pageable) {
        return examRepository.findAll(filter.toSpecification(), pageable)
                .map(examMapper::toResponse);
    }

    @Override
    public DetailedExamResponse getExamById(Long id) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not found",
                        "Exam not found"));
        return examMapper.toDetailedResponse(exam);
    }

    @Override
    public ExamResponse createExam(ExamRequest request) {
        if (examRepository.existsByTitle(request.title())) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Input error",
                    "Exam title already exists");
        }
        ExamEntity exam = examMapper.toEntity(request);
        return examMapper.toResponse(examRepository.save(exam));
    }

    @Override
    public ExamResponse updateExam(Long id, ExamRequest request) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not found",
                        "Exam not found"));
        if (examRepository.existsByCodeAndIdNot(request.code(), id)) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Input error",
                    "Exam code already exists");
        }
        examMapper.updateEntityFromRequest(request, exam);
        return examMapper.toResponse(examRepository.save(exam));
    }

    @Override
    public ExamResponse changeStatus(Long id) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not found",
                        "Exam not found"));

        ExamStatus newStatus = (exam.getExamStatus() == ExamStatus.DRAFT)
                ? ExamStatus.PUBLISHED
                : ExamStatus.DRAFT;

        exam.setExamStatus(newStatus);

        return examMapper.toResponse(examRepository.save(exam));
    }

    @Override
    public void deleteExamKeepsParts(Long id) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Data error",
                        "Exam not found"));

        if (exam.getParts() != null) {
            for (PartEntity part : exam.getParts()) {
                part.setExam(null);
                partRepository.save(part);
            }
        }
        examRepository.deleteById(id);
    }
}
