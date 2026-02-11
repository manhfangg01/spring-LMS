package com.quiz.quizproject.domain.sharedOption.service;

import com.quiz.quizproject.domain.sharedOption.dto.SharedOptionRequest;
import com.quiz.quizproject.domain.sharedOption.dto.SharedOptionResponse;
import com.quiz.quizproject.domain.sharedOption.entity.SharedOptionEntity;
import com.quiz.quizproject.domain.sharedOption.mapper.SharedOptionMapper;
import com.quiz.quizproject.domain.sharedOption.repository.SharedOptionRepository;
import com.quiz.quizproject.util.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedOptionService {
    private final SharedOptionRepository sharedOptionRepository;
    private final SharedOptionMapper sharedOptionMapper;

    @Transactional
    public SharedOptionResponse createSharedOption(SharedOptionRequest request) {
        SharedOptionEntity entity = sharedOptionMapper.toEntity(request);
        SharedOptionEntity savedEntity = sharedOptionRepository.save(entity);
        return sharedOptionMapper.toResponse(savedEntity);
    }

    @Transactional(readOnly = true)
    public SharedOptionResponse getSharedOptionById(Long id) {
        SharedOptionEntity entity = sharedOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SharedOption", id));
        return sharedOptionMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<SharedOptionResponse> getAllSharedOptions() {
        return sharedOptionRepository.findAll().stream()
                .map(sharedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SharedOptionResponse> getSharedOptionsByGroup(String optionGroup) {
        return sharedOptionRepository.findByOptionGroup(optionGroup).stream()
                .map(sharedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SharedOptionResponse updateSharedOption(Long id, SharedOptionRequest request) {
        SharedOptionEntity entity = sharedOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SharedOption", id));
        
        entity.setContent(request.getContent());
        entity.setLabel(request.getLabel());
        entity.setOptionGroup(request.getOptionGroup());
        
        SharedOptionEntity updatedEntity = sharedOptionRepository.save(entity);
        return sharedOptionMapper.toResponse(updatedEntity);
    }

    @Transactional
    public void deleteSharedOption(Long id) {
        if (!sharedOptionRepository.existsById(id)) {
            throw new EntityNotFoundException("SharedOption", id);
        }
        sharedOptionRepository.deleteById(id);
    }
}
