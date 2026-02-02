package com.quiz.quizproject.domain.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class HiController {
    @GetMapping("/hi")
    public String sayHi() {
        return "Hi there!";
    }
}
