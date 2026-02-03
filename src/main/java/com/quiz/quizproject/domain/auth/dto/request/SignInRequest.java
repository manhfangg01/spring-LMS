package com.quiz.quizproject.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotBlank(message = "Password cannot be empty") @Size(min = 6, message = "Password must be at least 6 characters") String password,
        @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format") String email) {
}
