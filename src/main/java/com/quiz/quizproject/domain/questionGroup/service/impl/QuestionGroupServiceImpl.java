package com.quiz.quizproject.domain.questionGroup.service.impl;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.repository.PartRepository;
import com.quiz.quizproject.domain.questionGroup.dto.request.MoveGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.DetailedQuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.entity.QuestionGroupEntity;
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
@Transactional
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
    public DetailedQuestionGroupResponse getQuestionGroupById(Long id) {
        QuestionGroupEntity questionGroup = questionGroupRepo.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question group not found"));
        return questionGroupMapper.toDetailedResponse(questionGroup);
    }

    @Override
    public QuestionGroupResponse createQuestionGroup(QuestionGroupRequest request, Long partId) {
        QuestionGroupEntity questionGroup = questionGroupMapper.toEntity(request);
        if (partId != null) {
            PartEntity part = partRepo.findById(partId)
                    .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                            "Part not found"));
            questionGroup.setPart(part);
        }
        return questionGroupMapper.toResponse(questionGroupRepo.save(questionGroup));
    }

    @Override
    public QuestionGroupResponse updateQuestionGroup(Long id, QuestionGroupRequest request) {
        QuestionGroupEntity questionGroup = questionGroupRepo.findById(id)
                .orElseThrow(() -> new AppException(
                        "ApiException", HttpStatus.NOT_FOUND, "Not Found", "Question group not found"));
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

    @Override
    public void moveGroups(Long groupId, MoveGroupRequest request) {
        QuestionGroupEntity groupToMove = questionGroupRepo.findById(groupId)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question group not found"));

        Long oldPartId = groupToMove.getPart().getId();
        Long newPartId = request.targetPartId();
        int newPos = request.position();

        if (!oldPartId.equals(newPartId)) {
            questionGroupRepo.decrementOrderIndex(oldPartId, groupToMove.getOrderIndex());
            PartEntity targetPart = new PartEntity();
            targetPart.setId(newPartId);
            groupToMove.setPart(targetPart);
        }

        questionGroupRepo.incrementOrderIndex(newPartId, newPos);
        groupToMove.setOrderIndex(newPos);

    }

}
