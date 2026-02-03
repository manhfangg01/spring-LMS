package com.quiz.quizproject.domain.part.service.impl;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.dto.request.PartRequest;
import com.quiz.quizproject.domain.part.dto.response.PartResponse;
import com.quiz.quizproject.domain.part.filter.PartFilter;
import com.quiz.quizproject.domain.part.mapper.PartMapper;
import com.quiz.quizproject.domain.part.repo.PartRepository;
import com.quiz.quizproject.domain.part.service.PartService;
import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.domain.exam.repo.ExamRepository;
import com.quiz.quizproject.util.exception.handler.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepository partRepo;
    private final PartMapper partMapper;
    private final ExamRepository examRepo;

    @Override
    @Transactional
    public PartResponse createPart(PartRequest req) {
        PartEntity part = partMapper.toEntity(req);
        if (req.examId() != null) {
            ExamEntity exam = examRepo.findById(req.examId())
                    .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                            "Exam not found"));
            part.setExam(exam);
        }
        return partMapper.toResponse(partRepo.save(part));
    }

    @Override
    public Page<PartResponse> getAllParts(Pageable pageable, PartFilter filter) {
        return partRepo.findAll(filter.toSpecification(), pageable)
                .map(partMapper::toResponse);
    }

    @Override
    public PartResponse getPartById(Long id) {
        PartEntity part = partRepo.findById(id)
                .orElseThrow(
                        () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found"));
        return partMapper.toResponse(part);
    }

    @Override
    @Transactional
    public PartResponse updatePart(Long id, PartRequest req) {
        PartEntity part = partRepo.findById(id)
                .orElseThrow(
                        () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found"));

        partMapper.updateEntityFromRequest(req, part);

        if (req.examId() != null) {
            ExamEntity exam = examRepo.findById(req.examId())
                    .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                            "Exam not found"));
            part.setExam(exam);
        } else {
            part.setExam(null);
        }

        return partMapper.toResponse(partRepo.save(part));
    }

    @Override
    public void deletePart(Long id) {
        if (!partRepo.existsById(id)) {
            throw new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found");
        }
        partRepo.deleteById(id);
    }
}
