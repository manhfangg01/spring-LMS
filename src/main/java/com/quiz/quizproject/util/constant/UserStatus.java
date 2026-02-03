package com.quiz.quizproject.util.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.quiz.quizproject.util.exception.handler.AppException;
import org.springframework.http.HttpStatus;

public enum UserStatus {
    ACTIVE,
    INACTIVE,
    BANNED;

    @JsonCreator
    public static UserStatus fromString(String value) {
        if (value == null)
            return null;
        try {
            return UserStatus.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new AppException("IllegalArgumentException", HttpStatus.BAD_REQUEST, "Input error",
                    "Invalid user status. Accepted values: ACTIVE, INACTIVE, BANNED");
        }
    }
}
