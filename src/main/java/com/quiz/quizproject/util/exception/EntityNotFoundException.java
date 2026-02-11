package com.quiz.quizproject.util.exception;

import com.quiz.quizproject.util.exception.handler.AppException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends AppException {
    public EntityNotFoundException(String entityName, Long id) {
        super("EntityNotFound", HttpStatus.NOT_FOUND, 
              entityName + " not found with id: " + id, null);
    }
}
