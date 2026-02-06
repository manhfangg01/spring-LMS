package com.quiz.quizproject.domain.exam.controller;

import com.quiz.quizproject.domain.exam.dto.request.ExamRequest;
import com.quiz.quizproject.domain.exam.dto.response.DetailedExamResponse;
import com.quiz.quizproject.domain.exam.dto.response.ExamResponse;
import com.quiz.quizproject.domain.exam.filter.ExamFilter;
import com.quiz.quizproject.domain.exam.service.ExamService;
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
@RequestMapping("api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examService.createExam(request));
    }

    @GetMapping
    public ResponseEntity<Page<ExamResponse>> getAllExams(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order,
            @ModelAttribute ExamFilter filterCriteria) {
        int pageIndex = page > 0 ? page - 1 : 0;
        Sort sort = Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageIndex, size, sort);
        return ResponseEntity.ok(examService.getExams(filterCriteria, pageable));
    }

    @GetMapping("/{examId}")
    public ResponseEntity<DetailedExamResponse> getExamById(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getExamById(examId));
    }

    @PatchMapping("/{examId}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Long examId,
            @Valid @RequestBody ExamRequest request) {
        return ResponseEntity.ok(examService.updateExam(examId, request));
    }

    @PatchMapping("{examId}/status")
    public ResponseEntity<ExamResponse> changeStatus(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.changeStatus(examId));
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) {
        examService.deleteExamKeepsParts(examId);
        return ResponseEntity.noContent().build();
    }
}
