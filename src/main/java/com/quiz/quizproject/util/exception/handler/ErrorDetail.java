package com.quiz.quizproject.util.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetail {
    private int status;
    private String message;
    private long timestamp;
}
