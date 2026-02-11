package com.quiz.quizproject.domain.question.service.impl;

import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import com.quiz.quizproject.domain.question.mapper.QuestionMapper;
import com.quiz.quizproject.domain.question.repository.QuestionRepository;
import com.quiz.quizproject.domain.question.service.QuestionService;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import com.quiz.quizproject.domain.questionGroup.repository.QuestionGroupRepository;
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
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuestionGroupRepository questionGroupRepository;

    @Override
    public DetailedQuestionResponse createQuestion(Long groupId, QuestionRequest request) {
        var questionGroup = questionGroupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question Group not found"));
        validateQuestionLimit(questionGroup);
        QuestionEntity question = questionMapper.toEntity(request);
        question.setType(questionGroup.getType());
        if(questionGroup.getType().equals(QuestionGroupType.MATCHING)){
            List<SharedOptionEntity> sharedOptions = questionGroup.getSharedOptions();
            Map<String, SharedOptionEntity> optionMap = sharedOptions.stream()
                    .collect(Collectors.toMap(SharedOptionEntity::getLabel, sharedOption -> sharedOption));
            String labelMatching = request.labelMatching();
            SharedOptionEntity optionMatching = optionMap.get(labelMatching);
            if(optionMatching == null){
                throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Bad Request",
                        "Valid labelMatching is required for MATCHING question type");
            }
            question.setCorrectSharedOption(optionMatching);
        }
        question.setQuestionGroup(questionGroup);
        int currentQuestionsCount = questionRepository.countByQuestionGroupId(groupId);
        int nextOrder = currentQuestionsCount + 1;
        question.setOrderIndex(nextOrder);
        question.setType(questionGroup.getType());
        return questionMapper.toResponse(questionRepository.save(question));
    }

    public void validateQuestionLimit(QuestionGroupEntity group) {
        int limit=13; //default limit
        int currentQuestionCount = questionRepository.countByQuestionGroupId(group.getId());
        if (currentQuestionCount >= limit) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Bad Request",
                    "Question limit exceeded for this group");
        }
    }

    @Override
    public DetailedQuestionResponse getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toResponse)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question not found"));
    }

    @Override
    public Page<DetailedQuestionResponse> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable)
                .map(questionMapper::toResponse);
    }

    @Override
    public DetailedQuestionResponse updateQuestion(Long id, QuestionRequest request) {
        QuestionEntity question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question not found"));

        questionMapper.updateEntityFromRequest(request, question);

//        if (request.questionOptions() != null) {
//            question.getQuestionOptions().clear();
//            var newOptions = request.questionOptions().stream()
//                    .map(questionMapper::toOptionEntity)
//                    .toList();
//            newOptions.forEach(question::addOption);
//        }
        return questionMapper.toResponse(questionRepository.save(question));
    }

    @Override
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found", "Question not found");
        }
        questionRepository.deleteById(id);
    }

    @Override
    public void reorderQuestions(Long groupId, List<Long> orderedIds) {
        List<QuestionEntity> questions = questionRepository.findAllById(orderedIds);
        Map<Long, QuestionEntity> questionMap= questions.stream().collect(Collectors.toMap(QuestionEntity::getId, questionGroup -> questionGroup));

        for(int i=0;i<orderedIds.size();i++){
            Long id =orderedIds.get(i);
            QuestionEntity question =questionMap.get(id);

            if (question !=null){
                question.setOrderIndex(i+1);
            }
        }
    }

    @Override
    public void moveQuestionToAnotherGroup(Long questionId, Long targetGroupId) {
        QuestionEntity questionToMove = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Question not found"));

        QuestionGroupEntity oldGroup = questionToMove.getQuestionGroup();
        Integer removedIndex = questionToMove.getOrderIndex();

        if(oldGroup.getId().equals(targetGroupId)){
            return;
        }

        QuestionGroupEntity targetGroup = questionGroupRepository.findById(targetGroupId)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                        "Target Question Group not found"));

        validateQuestionLimit(targetGroup);

        Integer maxOrderInTargetGroup = questionRepository.getMaxOrderIndexByQuestionGroupId(targetGroup.getId()).orElse(0);
        Integer nextOrderIndex = maxOrderInTargetGroup + 1;
        questionToMove.setQuestionGroup(targetGroup);
        questionToMove.setOrderIndex(nextOrderIndex);
        questionRepository.decreaseOrderIndexOnQuestionGroup(targetGroupId, removedIndex);
        questionRepository.save(questionToMove);
    }


}
