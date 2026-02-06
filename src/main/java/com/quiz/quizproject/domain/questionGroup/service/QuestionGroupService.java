package com.quiz.quizproject.domain.questionGroup.service;

import com.quiz.quizproject.domain.questionGroup.dto.request.QuestionGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.DetailedQuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.filter.QuestionGroupFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionGroupService {
    Page<QuestionGroupResponse> getAllQuestionGroups(Pageable pageable, QuestionGroupFilter filter);

    DetailedQuestionGroupResponse getQuestionGroupById(Long id);

    QuestionGroupResponse createQuestionGroup(QuestionGroupRequest request, Long partId);

    QuestionGroupResponse updateQuestionGroup(Long id, QuestionGroupRequest request);

    void deleteQuestionGroup(Long id);

    void reorderQuestionGroups(Long partId, List<Long> orderIds);

    void moveGroups(Long groupId, Long targetPartId);
}
