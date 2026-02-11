package com.quiz.quizproject.domain.sharedOption.controller;

import com.quiz.quizproject.domain.sharedOption.dto.SharedOptionRequest;
import com.quiz.quizproject.domain.sharedOption.dto.SharedOptionResponse;
import com.quiz.quizproject.domain.sharedOption.service.SharedOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shared-options")
@RequiredArgsConstructor
public class SharedOptionController {
    private final SharedOptionService sharedOptionService;

    @PostMapping
    public ResponseEntity<SharedOptionResponse> createSharedOption(@Valid @RequestBody SharedOptionRequest request) {
        SharedOptionResponse response = sharedOptionService.createSharedOption(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SharedOptionResponse> getSharedOptionById(@PathVariable Long id) {
        SharedOptionResponse response = sharedOptionService.getSharedOptionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SharedOptionResponse>> getAllSharedOptions(
            @RequestParam(required = false) String optionGroup) {
        List<SharedOptionResponse> responses;
        if (optionGroup != null && !optionGroup.isEmpty()) {
            responses = sharedOptionService.getSharedOptionsByGroup(optionGroup);
        } else {
            responses = sharedOptionService.getAllSharedOptions();
        }
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SharedOptionResponse> updateSharedOption(
            @PathVariable Long id,
            @Valid @RequestBody SharedOptionRequest request) {
        SharedOptionResponse response = sharedOptionService.updateSharedOption(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSharedOption(@PathVariable Long id) {
        sharedOptionService.deleteSharedOption(id);
        return ResponseEntity.noContent().build();
    }
}
