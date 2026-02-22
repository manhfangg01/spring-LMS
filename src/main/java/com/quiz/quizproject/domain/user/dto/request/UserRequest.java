package com.quiz.quizproject.domain.user.dto.request;

import com.quiz.quizproject.util.constant.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record UserRequest(
                @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format")
                String email,
                @Size(min = 6, message = "Password must be at least 6 characters")
                String password,
                @Size(min = 5, message = "UserName must be at least 5 characters")
                String userName,
                @NotBlank(message = "roleName cannot be empty")
                String roleName,
                @NotNull(message = "Status cannot be empty")
                UserStatus status

) {
}
