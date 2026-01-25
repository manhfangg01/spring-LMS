package com.quiz.quizproject.service.auth;

import com.quiz.quizproject.entity.dto.request.AuthRequest;
import com.quiz.quizproject.entity.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public String singUp(AuthRequest authRequest);
    public AuthResponse signIn(AuthRequest authRequest);
    public AuthResponse refreshToken(String refreshToken);
    public void signOut(String refreshToken);
}
