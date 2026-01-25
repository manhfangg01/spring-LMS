package com.quiz.quizproject.controller.auth;

import com.quiz.quizproject.entity.dto.request.AuthRequest;
import com.quiz.quizproject.entity.dto.response.AuthResponse;
import com.quiz.quizproject.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${REFRESH_VALID_TIME}")
    private long refreshValidTime;

    private final AuthService authService;

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (refreshValidTime / 1000));
        response.addCookie(cookie);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.singUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.signIn(request);

        // Lưu Refresh Token vào HttpOnly Cookie
        setRefreshTokenCookie(response, authResponse.refreshToken());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);

        setRefreshTokenCookie(response, authResponse.refreshToken());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken != null) {
            authService.signOut(refreshToken);
        }

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(null);
    }
}