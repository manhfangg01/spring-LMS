package com.quiz.quizproject.domain.questionGroup.service;

import com.quiz.quizproject.domain.questionGroup.dto.request.QuestionGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.filter.QuestionGroupFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface QuestionGroupService {
    Page<QuestionGroupResponse> getAllQuestionGroups(Pageable pageable, QuestionGroupFilter filter);

    QuestionGroupResponse getQuestionGroupById(Long id);

    QuestionGroupResponse createQuestionGroup(QuestionGroupRequest request);

    QuestionGroupResponse updateQuestionGroup(Long id, QuestionGroupRequest request);

    void deleteQuestionGroup(Long id);
}
