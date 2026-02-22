package com.quiz.quizproject.domain.user.dto.response;

import com.quiz.quizproject.util.constant.UserStatus;
import lombok.Builder;
import lombok.With;

import java.time.LocalDateTime;

@Builder
@With
public record UserResponse(
        Long id,
        String email,
        String userName,
        String avatarUrl,
        String roleName,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
