package com.quiz.quizproject.domain.question.controller;

import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import com.quiz.quizproject.domain.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/questionGroups/{groupId}/questions")
    public ResponseEntity<DetailedQuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request,
            @PathVariable Long groupId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionService.createQuestion(groupId, request));
    }

    @GetMapping("questions")
    public ResponseEntity<Page<DetailedQuestionResponse>> getAllQuestions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        int pageIndex = page > 0 ? page - 1 : 0;
        Sort sort = Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageIndex, size, sort);
        return ResponseEntity.ok(questionService.getAllQuestions(pageable));
    }

    @GetMapping("questions/{questionId}")
    public ResponseEntity<DetailedQuestionResponse> getQuestionById(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }

    @PutMapping("questions/{questionId}")
    public ResponseEntity<DetailedQuestionResponse> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, request));
    }

    @DeleteMapping("questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/questionGroups/{groupId}/questions/reorder")
    public ResponseEntity<Void> reorderQuestions(
            @PathVariable Long groupId,
            @RequestBody List<Long> orderedIds) {
        questionService.reorderQuestions(groupId, orderedIds);
        return ResponseEntity.noContent().build();
    }
}
