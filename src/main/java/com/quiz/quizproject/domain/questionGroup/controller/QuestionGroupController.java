package com.quiz.quizproject.domain.questionGroup.controller;

import com.quiz.quizproject.domain.questionGroup.dto.request.QuestionGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.filter.QuestionGroupFilter;
import com.quiz.quizproject.domain.questionGroup.service.QuestionGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class QuestionGroupController {
    private final QuestionGroupService questionGroupService;

    @PostMapping("/parts/{partId}/groups")
    public ResponseEntity<QuestionGroupResponse> createQuestionGroup(@Valid @RequestBody QuestionGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionGroupService.createQuestionGroup(request));
    }

    @GetMapping("groups")
    public ResponseEntity<Page<QuestionGroupResponse>> getAllQuestionGroups(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute QuestionGroupFilter filterCriteria) {
        int pageIndex = page > 0 ? page - 1 : 0;
        Sort sort = Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageIndex, size, sort);
        return ResponseEntity.ok(questionGroupService.getAllQuestionGroups(pageable, filterCriteria));
    }

    @GetMapping("groups/{groupId}")
    public ResponseEntity<QuestionGroupResponse> getQuestionGroupById(@PathVariable Long groupId) {
        return ResponseEntity.ok(questionGroupService.getQuestionGroupById(groupId));
    }

    @PutMapping("groups/{groupId}")
    public ResponseEntity<QuestionGroupResponse> updateQuestionGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody QuestionGroupRequest request) {
        return ResponseEntity.ok(questionGroupService.updateQuestionGroup(groupId, request));
    }

    @DeleteMapping("groups/{groupId}")
    public ResponseEntity<Void> deleteQuestionGroup(@PathVariable Long groupId) {
        questionGroupService.deleteQuestionGroup(groupId);
        return ResponseEntity.noContent().build();
    }
}
