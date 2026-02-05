package com.quiz.quizproject.domain.question.service.impl;

import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import com.quiz.quizproject.domain.question.mapper.QuestionMapper;
import com.quiz.quizproject.domain.question.repository.QuestionRepository;
import com.quiz.quizproject.domain.question.service.QuestionService;
import com.quiz.quizproject.domain.questionGroup.repository.QuestionGroupRepository;
import com.quiz.quizproject.util.exception.handler.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuestionGroupRepository questionGroupRepository;

    @Override
    public DetailedQuestionResponse createQuestion(Long groupId,QuestionRequest request) {
        QuestionEntity question = questionMapper.toEntity(request);
        if (groupId != null) {
            var questionGroup = questionGroupRepository.findById(groupId)
                    .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not Found",
                            "Question Group not found"));
            question.setQuestionGroup(questionGroup);
        }
        return questionMapper.toResponse(questionRepository.save(question));
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

        if (request.questionOptions() != null) {
            question.getQuestionOptions().clear();
            var newOptions = request.questionOptions().stream()
                    .map(questionMapper::toOptionEntity)
                    .toList();
            newOptions.forEach(question::addOption);
        }

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

    }
}
