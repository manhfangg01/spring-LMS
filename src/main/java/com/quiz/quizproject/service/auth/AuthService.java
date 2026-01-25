package com.quiz.quizproject.service.auth;

import com.quiz.quizproject.entity.dto.request.SignInRequest;
import com.quiz.quizproject.entity.dto.request.SignUpRequest;
import com.quiz.quizproject.entity.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public String signUp(SignUpRequest signUpRequest);
    public AuthResponse signIn(SignInRequest signUpRequest);
    public AuthResponse refreshToken(String refreshToken);
    public void signOut(String refreshToken);
}
