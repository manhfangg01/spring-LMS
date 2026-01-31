package com.quiz.quizproject.entity.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
      @NotBlank(message = "Username không được để trống")
      String userName,
      @NotBlank(message = "Password không được để trống")
      @Size(min = 6, message = "Mật khẩu phải từ 6 ký tự")
      String password,
      @NotBlank(message = "Email không được để trống")
      @Email(message = "Email không đúng định dạng")
      String email)
{}