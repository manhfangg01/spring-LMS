package com.quiz.quizproject.domain.test.controller;

import com.quiz.quizproject.domain.part.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
@RequiredArgsConstructor
public class HiController {
    private final PartRepository partRepository;

    @GetMapping("/hi")
    public void sayHi() {

    }
}
