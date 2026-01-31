package com.quiz.quizproject.service.auth;

import com.quiz.quizproject.domain.auth.dto.request.SignInRequest;
import com.quiz.quizproject.domain.auth.dto.request.SignUpRequest;
import com.quiz.quizproject.domain.auth.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String signUp(SignUpRequest signUpRequest);
    AuthResponse signIn(SignInRequest signUpRequest);
    AuthResponse refreshToken(String refreshToken);
    void signOut(String refreshToken);
}
