package com.quiz.quizproject.entity.dto.response;

public record AuthResponse(String tokenType, String accessToken, String refreshToken) {
    public static AuthResponse of(String accessToken,String refreshToken) {
        return new AuthResponse("Bearer", accessToken, refreshToken );
    }
}