package com.quiz.quizproject.domain.part.service.impl;

import com.quiz.quizproject.domain.exam.repository.ExamRepository;
import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.dto.request.PartRequest;
import com.quiz.quizproject.domain.part.dto.response.DetailedPartResponse;
import com.quiz.quizproject.domain.part.dto.response.PartResponse;
import com.quiz.quizproject.domain.part.filter.PartFilter;
import com.quiz.quizproject.domain.part.mapper.PartMapper;
import com.quiz.quizproject.domain.part.repository.PartRepository;
import com.quiz.quizproject.domain.part.service.PartService;
import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.util.constant.ExamType;
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

    private final PartRepository partRepository;
    private final PartMapper partMapper;
    private final ExamRepository examRepo;

    @Override
    public PartResponse createPart(PartRequest request, Long examId) {
            PartEntity newPart = partMapper.toEntity(request);
            ExamEntity exam = examRepo.findById(examId)
                    .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                            "Exam not found"));
            validatePartLimit(exam);
            newPart.setExamType(exam.getExamType());
            int currentPartsCount = partRepository.countByExamId(examId);
            int nextOrder = currentPartsCount + 1;
            newPart.setExam(exam);
            newPart.setOrderIndex(nextOrder);
        return partMapper.toResponse(partRepository.save(newPart));
    }

    public void validatePartLimit(ExamEntity exam){
        int currentCount = partRepository.countByExamId(exam.getId());
        int limit = getLimitByExamType(exam.getExamType());

        if (currentCount >= limit) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Reach limit",
                    "Cannot add more parts to this exam type. Limit is " + limit);
        }
    }

    private int getLimitByExamType(ExamType examType) {
        return switch (examType) {
            case READING, SPEAKING -> 3;
            case LISTENING -> 4;
            case WRITING -> 2;
        };
    }

    @Override
    public Page<PartResponse> getAllParts(Pageable pageable, PartFilter filter) {
        return partRepository.findAll(filter.toSpecification(), pageable)
                .map(partMapper::toResponse);
    }

    @Override
    public DetailedPartResponse getPartById(Long id) {
        PartEntity part = partRepository.findById(id)
                .orElseThrow(
                        () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found"));
        return partMapper.toDetailedResponse(part);
    }

        @Override
        public PartResponse updatePart(Long id, PartRequest request) {
            PartEntity part = partRepository.findById(id)
                    .orElseThrow(
                            () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found"));
            partMapper.updateEntityFromRequest(request, part);
            return partMapper.toResponse(partRepository.save(part));
    }

    @Override
    public void deletePart(Long id) {
        if (!partRepository.existsById(id)) {
            throw new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found");
        }
        partRepository.deleteById(id);
    }

    @Override
    public void reorderParts(Long examId, List<Long> orderedIds) {
        List<PartEntity> parts = partRepository.findAllByExamId(examId);
        Map<Long, PartEntity> partMap = parts.stream()
                .collect(Collectors.toMap(PartEntity::getId, p -> p));

        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            PartEntity part = partMap.get(id);

            if (part != null) {
                part.setOrderIndex(i + 1);
            }
        }
    }

}
