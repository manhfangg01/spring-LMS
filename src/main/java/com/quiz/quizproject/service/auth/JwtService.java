package com.quiz.quizproject.service.auth;

import com.quiz.quizproject.entity.RefreshTokenEntity;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public interface JwtService {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isRefreshTokenValid(String token, RefreshTokenEntity tokenEntity);

    boolean isAccessTokenValid(String token, UserDetails userDetails);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);


}
