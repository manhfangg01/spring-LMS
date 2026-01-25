package com.quiz.quizproject.entity.dto.response;

public record AuthResponse(String tokenType, String accessToken, String userName) {
    public static AuthResponse of(String token, String username) {
        return new AuthResponse(token, "Bearer", username);
    }
}