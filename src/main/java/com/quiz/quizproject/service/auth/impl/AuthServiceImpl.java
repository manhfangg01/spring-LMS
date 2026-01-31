package com.quiz.quizproject.service.auth.impl;

import com.quiz.quizproject.domain.RefreshTokenEntity;
import com.quiz.quizproject.domain.RoleEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.auth.dto.request.SignInRequest;
import com.quiz.quizproject.domain.auth.dto.request.SignUpRequest;
import com.quiz.quizproject.domain.auth.dto.response.AuthResponse;
import com.quiz.quizproject.repository.RefreshTokenRepository;
import com.quiz.quizproject.repository.RoleRepository;
import com.quiz.quizproject.repository.UserRepository;
import com.quiz.quizproject.service.auth.AuthService;
import com.quiz.quizproject.service.auth.JwtService;
import com.quiz.quizproject.util.constant.UserStatus;
import com.quiz.quizproject.util.exception.handler.AppException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
            throw new AppException("ApiException", HttpStatus.UNAUTHORIZED,"Lỗi đăng kí", "Email đã được sử dụng!");
        }

        String finalUserName = generateUniqueUserName(request.userName());

        UserEntity user = new UserEntity();
        user.setUserName(finalUserName);
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.ACTIVE);

        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.UNAUTHORIZED,"Role không tồn tại!", null));
        user.setRole(userRole);

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
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.UNAUTHORIZED,"Email người dùng không không tồn tại!",null));

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
                .orElseThrow(() -> new AppException("TokenException", HttpStatus.UNAUTHORIZED,"RefreshToken không tồn tại!", null));

        if (!jwtService.isRefreshTokenValid(requestRefreshToken, tokenEntity)) {
            refreshTokenRepository.delete(tokenEntity);
            throw new AppException("TokenException", HttpStatus.UNAUTHORIZED,"RefreshToken không đúng hoặc hết hạn!",null);
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
                .authorities(new SimpleGrantedAuthority(user.getRole().getName()))
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