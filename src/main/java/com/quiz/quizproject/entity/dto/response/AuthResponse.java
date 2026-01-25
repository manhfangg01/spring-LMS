package com.quiz.quizproject.entity.dto.response;

public record AuthResponse(String tokenType, String accessToken, String refreshToken, String userName) {
    public static AuthResponse of(String accessToken,String refreshToken, String username) {
        return new AuthResponse("Bearer", accessToken, refreshToken , username);
    }
}