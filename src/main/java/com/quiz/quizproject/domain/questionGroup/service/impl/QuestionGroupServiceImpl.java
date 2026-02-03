package com.quiz.quizproject.domain.questionGroup.service.impl;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.repo.PartRepository;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import com.quiz.quizproject.domain.questionGroup.dto.request.QuestionGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.filter.QuestionGroupFilter;
import com.quiz.quizproject.domain.questionGroup.mapper.QuestionGroupMapper;
import com.quiz.quizproject.domain.questionGroup.repository.QuestionGroupRepository;
import com.quiz.quizproject.domain.questionGroup.service.QuestionGroupService;
import com.quiz.quizproject.util.exception.handler.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionGroupServiceImpl implements QuestionGroupService {

    private final QuestionGroupRepository questionGroupRepo;
    private final QuestionGroupMapper questionGroupMapper;
    private final PartRepository partRepo;

    @Override
    public Page<QuestionGroupResponse> getAllQuestionGroups(Pageable pageable, QuestionGroupFilter filter) {
        return questionGroupRepo.findAll(filter.toSpecification(), pageable)
                .map(questionGroupMapper::toResponse);
    }

    @Override
    public QuestionGroupResponse getQuestionGroupById(Long id) {
        QuestionGroupEntity questionGroup = questionGroupRepo.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question group not found"));
        return questionGroupMapper.toResponse(questionGroup);
    }

    @Override
    @Transactional
    public QuestionGroupResponse createQuestionGroup(QuestionGroupRequest request) {
        QuestionGroupEntity questionGroup = questionGroupMapper.toEntity(request);
        if (request.partId() != null) {
            PartEntity part = partRepo.findById(request.partId())
                    .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                            "Part not found"));
            questionGroup.setPart(part);
        }
        return questionGroupMapper.toResponse(questionGroupRepo.save(questionGroup));
    }

    @Override
    @Transactional
    public QuestionGroupResponse updateQuestionGroup(Long id, QuestionGroupRequest request) {
        QuestionGroupEntity questionGroup = questionGroupRepo.findById(id)
                .orElseThrow(() -> new AppException(
                        "ApiException", HttpStatus.NOT_FOUND, "Not Found", "Question group not found"));

        Long currentPartId = questionGroup.getPart() != null ? questionGroup.getPart().getId() : null;

        if (request.partId() != null && !request.partId().equals(currentPartId)) {
            PartEntity newPart = partRepo.findById(request.partId())
                    .orElseThrow(() -> new AppException(
                            "ApiException", HttpStatus.NOT_FOUND, "Not Found", "Part not found with id: " + request.partId()));

            questionGroup.setPart(newPart);
        }

        questionGroupMapper.updateEntityFromRequest(request, questionGroup);

        QuestionGroupEntity savedGroup = questionGroupRepo.save(questionGroup);
        return questionGroupMapper.toResponse(savedGroup);
    }
    @Override
    public void deleteQuestionGroup(Long id) {
        if (!questionGroupRepo.existsById(id)) {
            throw new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Question group not found");
        }
        questionGroupRepo.deleteById(id);
    }
}
