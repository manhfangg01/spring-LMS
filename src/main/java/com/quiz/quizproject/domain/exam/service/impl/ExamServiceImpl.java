package com.quiz.quizproject.domain.exam.service.impl;

import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.domain.exam.dto.request.ExamRequest;
import com.quiz.quizproject.domain.exam.dto.response.DetailedExamResponse;
import com.quiz.quizproject.domain.exam.dto.response.ExamResponse;
import com.quiz.quizproject.domain.exam.filter.ExamFilter;
import com.quiz.quizproject.domain.exam.mapper.ExamMapper;
import com.quiz.quizproject.domain.exam.repo.ExamRepository;
import com.quiz.quizproject.domain.exam.service.ExamService;
import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.repo.PartRepository;
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
    private  final PartRepository partRepository;

    @Override
    public Page<ExamResponse> getExams(ExamFilter filter, Pageable pageable) {
        return examRepository.findAll(filter.toSpecification(), pageable)
                .map(examMapper::toResponse);
    }

    @Override
    public DetailedExamResponse getExamById(Long id) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Không tìm thấy",
                        "Bài thi không tồn tại"));
        return examMapper.toDetailedResponse(exam);
    }

    @Override
    public ExamResponse createExam(ExamRequest request) {
        if (examRepository.existsByTitle(request.title())) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Lỗi nhập liệu",
                    "Tiêu đề bài thi đã tồn tại");
        }
        ExamEntity exam = examMapper.toEntity(request);
        return examMapper.toResponse(examRepository.save(exam));
    }

    @Override
    public ExamResponse updateExam(Long id, ExamRequest request) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Không tìm thấy",
                        "Bài thi không tồn tại"));
        if (examRepository.existsByCodeAndIdNot(request.code(), id)) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Lỗi nhập liệu",
                    "Tiêu đề bài thi đã tồn tại");
        }
        examMapper.updateEntityFromRequest(request, exam);
        return examMapper.toResponse(examRepository.save(exam));
    }

    @Override
    public void deleteExamKeepsParts(Long id) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Lỗi dữ liệu",
                        "Bài thi không tồn tại"));

        if (exam.getParts() != null) {
            for (PartEntity part : exam.getParts()) {
                part.setExam(null);
                partRepository.save(part);
            }
        }
        examRepository.deleteById(id);
    }
}
