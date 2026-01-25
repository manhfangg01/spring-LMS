package com.quiz.quizproject.util.exception.handler;

import com.quiz.quizproject.util.exception.ApiException;
import com.quiz.quizproject.util.exception.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDetail> handleApiException(ApiException ex) {
        ErrorDetail error = new ErrorDetail(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorDetail> handleTokenException(TokenException ex) {
        ErrorDetail error = new ErrorDetail(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetail> handleBadCredentials(BadCredentialsException ex) {
        ErrorDetail error = new ErrorDetail(
                HttpStatus.UNAUTHORIZED.value(),
                "Thông tin đăng nhập không hợp lệ",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleGlobalException(Exception ex) {
        ErrorDetail error = new ErrorDetail(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Đã có lỗi hệ thống xảy ra: " + ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
