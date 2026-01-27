package com.quiz.quizproject.util.exception.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final String name;
    private final HttpStatus statusCode;
    private final Object error;

    public AppException(String name, HttpStatus statusCode, String message, Object error) {
        this.statusCode = statusCode;
        super(message);
        this.name = name;
        this.error = error;
    }
}
