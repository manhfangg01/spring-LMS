package com.quiz.quizproject.domain.part.service.impl;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.dto.request.PartRequest;
import com.quiz.quizproject.domain.part.dto.response.DetailedPartResponse;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepository partRepo;
    private final PartMapper partMapper;
    private final ExamRepository examRepo;

    @Override
    public PartResponse createPart(PartRequest req, Long examId) {
        PartEntity part = partMapper.toEntity(req);
        if (examId != null) {
            ExamEntity exam = examRepo.findById(examId)
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
    public DetailedPartResponse getPartById(Long id) {
        PartEntity part = partRepo.findById(id)
                .orElseThrow(
                        () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found"));
        return partMapper.toDetailedResponse(part);
    }

    @Override
    public PartResponse updatePart(Long id, PartRequest req) {
        PartEntity part = partRepo.findById(id)
                .orElseThrow(
                        () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found"));
        partMapper.updateEntityFromRequest(req, part);
        return partMapper.toResponse(partRepo.save(part));
    }

    @Override
    public void deletePart(Long id) {
        if (!partRepo.existsById(id)) {
            throw new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found");
        }
        partRepo.deleteById(id);
    }

    @Override
    public void reorderParts(Long examId, List<Long> orderedIds) {
        List<PartEntity> parts = partRepo.findAllByExamId(examId);
        Map<Long, PartEntity> partMap = parts.stream()
                .collect(Collectors.toMap(PartEntity::getId, p -> p));

        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            PartEntity part = partMap.get(id);

            if (part != null) {
                part.setOrderIndex(i);
            }
        }
    }


}
