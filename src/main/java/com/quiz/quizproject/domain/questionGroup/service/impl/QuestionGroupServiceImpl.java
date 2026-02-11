package com.quiz.quizproject.domain.questionGroup.service.impl;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.repository.PartRepository;
import com.quiz.quizproject.domain.questionGroup.dto.response.DetailedQuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import com.quiz.quizproject.domain.questionGroup.dto.request.QuestionGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.filter.QuestionGroupFilter;
import com.quiz.quizproject.domain.questionGroup.mapper.QuestionGroupMapper;
import com.quiz.quizproject.domain.questionGroup.repository.QuestionGroupRepository;
import com.quiz.quizproject.domain.questionGroup.service.QuestionGroupService;
import com.quiz.quizproject.domain.sharedOption.SharedOptionEntity;
import com.quiz.quizproject.util.constant.QuestionGroupType;
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
public class QuestionGroupServiceImpl implements QuestionGroupService {

    private final QuestionGroupRepository questionGroupRepository;
    private final QuestionGroupMapper questionGroupMapper;
    private final PartRepository partRepository;

    @Override
    public Page<QuestionGroupResponse> getAllQuestionGroups(Pageable pageable, QuestionGroupFilter filter) {
        return questionGroupRepository.findAll(filter.toSpecification(), pageable)
                .map(questionGroupMapper::toResponse);
    }

    @Override
    public DetailedQuestionGroupResponse getQuestionGroupById(Long id) {
        QuestionGroupEntity questionGroup = questionGroupRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question group not found"));
        return questionGroupMapper.toDetailedResponse(questionGroup);
    }

    @Override
    public QuestionGroupResponse createQuestionGroup(QuestionGroupRequest request, Long partId) {
        validateMatchingType(request);
        PartEntity part = partRepository.findById(partId)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Part not found"));
        validateGroupLimit(part);
        QuestionGroupEntity questionGroup = questionGroupMapper.toEntity(request);
        int currentGroupsCount = questionGroupRepository.countByPartId(partId);
        int nextOrder = currentGroupsCount + 1;
        questionGroup.setPart(part);
        questionGroup.setOrderIndex(nextOrder);
        return questionGroupMapper.toResponse(questionGroupRepository.save(questionGroup));
    }

    public void validateMatchingType(QuestionGroupRequest request){
        QuestionGroupType questionGroupType = request.type();
        List<SharedOptionEntity> sharedOptions = request.sharedOptions();
        if(questionGroupType.equals(QuestionGroupType.MATCHING) && (sharedOptions == null || sharedOptions.isEmpty())){
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Invalid Data",
                    "Matching question groups must have shared options defined.");
        }
    }


    public void validateGroupLimit(PartEntity part) {
        int currentGroupCount = questionGroupRepository.countByPartId(part.getId());
        int maxGroupsAllowed = 5;
        if (currentGroupCount >= maxGroupsAllowed) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Limit Exceeded",
                    "Maximum number of question groups reached for this part.");
        }
    }

    @Override
    public QuestionGroupResponse updateQuestionGroup(Long id, QuestionGroupRequest request) {
        QuestionGroupEntity questionGroup = questionGroupRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        "ApiException", HttpStatus.NOT_FOUND, "Not Found", "Question group not found"));
        questionGroupMapper.updateEntityFromRequest(request, questionGroup);

        QuestionGroupEntity savedGroup = questionGroupRepository.save(questionGroup);
        return questionGroupMapper.toResponse(savedGroup);
    }

    @Override
    public void deleteQuestionGroup(Long id) {
        if (!questionGroupRepository.existsById(id)) {
            throw new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Question group not found");
        }
        questionGroupRepository.deleteById(id);
    }

    @Override
    public void reorderQuestionGroups(Long partId, List<Long> orderIds) {
        List<QuestionGroupEntity> questionGroups = questionGroupRepository.findAllById(orderIds);
        Map<Long,QuestionGroupEntity> groupMap= questionGroups.stream().collect(Collectors.toMap(QuestionGroupEntity::getId, questionGroup -> questionGroup));

        for (int i=0;i<orderIds.size();i++){
            Long id = orderIds.get(i);
            QuestionGroupEntity group = groupMap.get(id);

            if (group !=null){
                group.setOrderIndex(i+1);
            }
        }
    }

    @Override
    public void moveGroups(Long groupId, Long targetPartId) {
        QuestionGroupEntity groupToMove = questionGroupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question group not found"));



        // Lưu lại thông tin về vị trí trước khi di dời
        PartEntity oldPart = groupToMove.getPart();
        Integer removedIndex = groupToMove.getOrderIndex();

        // check nếu Id của nơi đi và nơi đến giống nhau thì không cần làm gì cả
        if (oldPart.getId().equals(targetPartId)) {
            return;
        }

        // Lấy ra đối tượng đích
        PartEntity targetPart = partRepository.findById(targetPartId)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Target part not found"));

        validateGroupLimit(targetPart);

        // Lấy ra số order index lớn nhất giữa group tại part đích
        Integer nextGroupsCount = questionGroupRepository.getMaxOrderIndexByPartId(targetPartId).orElse(0) + 1;

        // Gán lại part mới cho group sắp chuyển đi
        groupToMove.setPart(targetPart);
        // Đặt cho group mới luôn nằm ở vị trí cuối cùng ở part đích
        groupToMove.setOrderIndex(nextGroupsCount);
        questionGroupRepository.save(groupToMove);

        // Giảm order index của các group phía sau trong part cũ đi một đơn vị
        // Case 1: Nếu như group được di dời là group cuối cùng trong part cũ thì không cần giảm
        // Case 2: Nếu group được di dời ở đầu thì phải giảm tất cả các group phía sau: 2,3 -> 1,2
        // Case 3: Nếu group được di dời ở giữa thì cũng giảm tất cả các group phía sau: 1,2,3 -> 1,2
        questionGroupRepository.decreaseOrderIndexOnPart(oldPart.getId(), removedIndex);
    }
}
