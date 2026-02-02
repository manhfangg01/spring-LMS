package com.quiz.quizproject.domain.user.dto.request;

import com.quiz.quizproject.util.constant.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không đúng định dạng")
        String email,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 6, message = "Mật khẩu phải từ 6 ký tự")
        String password,

        @NotBlank(message = "Họ tên không được để trống")
        String userName,
        String roleName,
        UserStatus status

) {
}
