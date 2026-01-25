package com.quiz.quizproject.service.auth.impl;

import com.quiz.quizproject.entity.RefreshTokenEntity;
import com.quiz.quizproject.entity.RoleEntity;
import com.quiz.quizproject.entity.UserEntity;
import com.quiz.quizproject.entity.dto.request.SignInRequest;
import com.quiz.quizproject.entity.dto.request.SignUpRequest;
import com.quiz.quizproject.entity.dto.response.AuthResponse;
import com.quiz.quizproject.repository.RefreshTokenRepository;
import com.quiz.quizproject.repository.RoleRepository;
import com.quiz.quizproject.repository.UserRepository;
import com.quiz.quizproject.service.auth.AuthService;
import com.quiz.quizproject.service.auth.JwtService;
import com.quiz.quizproject.util.exception.ApiException;
import com.quiz.quizproject.util.exception.TokenException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${REFRESH_VALID_TIME}")
    private long refreshValidTime;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public String signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException("Email đã được sử dụng!");
        }

        String finalUserName = generateUniqueUserName(request.userName());

        UserEntity user = new UserEntity();
        user.setUserName(finalUserName);
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus("ACTIVE");

        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ApiException("Role không tồn tại"));
        user.setRoles(List.of(userRole));

        userRepository.save(user);

        return "Chúc mừng đã đăng ký thành công, với username: " + finalUserName;
    }

    @Override
    @Transactional
    public AuthResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("User không tồn tại"));

        UserDetails userDetails = buildUserDetails(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshTokenStr = jwtService.generateRefreshToken(userDetails);

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshTokenEntity());

        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setValue(refreshTokenStr);
        refreshTokenEntity.setExpiryDate(Instant.now().plusMillis(refreshValidTime));

        refreshTokenRepository.save(refreshTokenEntity);

        return AuthResponse.of(accessToken, refreshTokenStr);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String requestRefreshToken) {
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByValue(requestRefreshToken)
                .orElseThrow(() -> new TokenException("Refresh token không tồn tại!"));

        if (!jwtService.isRefreshTokenValid(requestRefreshToken, tokenEntity)) {
            refreshTokenRepository.delete(tokenEntity);
            throw new TokenException("Refresh token không hợp lệ hoặc đã hết hạn!");
        }

        UserEntity user = tokenEntity.getUser();
        UserDetails userDetails = buildUserDetails(user);

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        tokenEntity.setValue(newRefreshToken);
        tokenEntity.setExpiryDate(Instant.now().plusMillis(refreshValidTime));

        refreshTokenRepository.save(tokenEntity);

        return AuthResponse.of(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public void signOut(String refreshToken) {
        refreshTokenRepository.findByValue(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    private UserDetails buildUserDetails(UserEntity user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r.getName())).toList())
                .build();
    }

    private String generateUniqueUserName(String baseName) {
        String newName = baseName;
        Random random = new Random();
        while (userRepository.existsByUserName(newName)) {
            int randomNumber = random.nextInt(1000, 9999);
            newName = baseName + randomNumber;
        }
        return newName;
    }
}