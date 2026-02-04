package com.quiz.quizproject.domain.part.controller;

import com.quiz.quizproject.domain.part.dto.request.PartRequest;
import com.quiz.quizproject.domain.part.dto.response.DetailedPartResponse;
import com.quiz.quizproject.domain.part.dto.response.PartResponse;
import com.quiz.quizproject.domain.part.filter.PartFilter;
import com.quiz.quizproject.domain.part.service.PartService;
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
public class PartController {
    private final PartService partService;

    @PostMapping("/exams/{id}/parts")
    public ResponseEntity<PartResponse> createPart(@Valid @RequestBody PartRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partService.createPart(request, id));
    }

    @GetMapping("parts")
    public ResponseEntity<Page<PartResponse>> getAllParts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute PartFilter filterCriteria) {

        int pageIndex = page > 0 ? page - 1 : 0;
        Sort sort = Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageIndex, size, sort);

        return ResponseEntity.ok(partService.getAllParts(pageable, filterCriteria));
    }

    @GetMapping("parts/{partId}")
    public ResponseEntity<DetailedPartResponse> getPartById(@PathVariable Long partId) {
        return ResponseEntity.ok(partService.getPartById(partId));
    }

    @PutMapping("parts/{partId}")
    public ResponseEntity<PartResponse> updatePart(
            @PathVariable Long partId,
            @Valid @RequestBody PartRequest request) {
        return ResponseEntity.ok(partService.updatePart(partId, request));
    }

    @DeleteMapping("parts/{partId}")
    public ResponseEntity<Void> deletePart(@PathVariable Long partId) {
        partService.deletePart(partId);
        return ResponseEntity.noContent().build();
    }
}
