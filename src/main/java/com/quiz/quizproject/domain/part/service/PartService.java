package com.quiz.quizproject.domain.part.service;

import com.quiz.quizproject.domain.part.dto.request.PartRequest;
import com.quiz.quizproject.domain.part.dto.response.DetailedPartResponse;
import com.quiz.quizproject.domain.part.dto.response.PartResponse;
import com.quiz.quizproject.domain.part.filter.PartFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartService {
    Page<PartResponse> getAllParts(Pageable pageable, PartFilter filter);

    DetailedPartResponse getPartById(Long id);

    PartResponse createPart(PartRequest request, Long examId);

    PartResponse updatePart(Long id, PartRequest request);

    void deletePart(Long id);

    void reorderParts(Long examId, List<Long> orderedIds);
}
