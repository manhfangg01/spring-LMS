package com.quiz.quizproject.domain.dto;

import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.util.constant.UserStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

public class UserRequestTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Helper method để tạo một request hợp lệ mặc định.
     * Giúp giảm lặp code trong phần Arrange.
     */
    private UserRequest.UserRequestBuilder createValidRequestBuilder() {
        return UserRequest.builder()
                .email("manhphan@gmail.com")
                .password("password123")
                .userName("manhphan")
                .roleName("ADMIN")
                .status(UserStatus.ACTIVE);
    }

    // ----- EMAIL TEST -------

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "manhphan", "manhphan@", "@gmail.com"})
    public void email_ShouldHaveValidationErrors_WhenInvalidEmailGiven(String invalidEmail) {
        // I. Arrange
        UserRequest request = createValidRequestBuilder()
                .email(invalidEmail)
                .build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations).anySatisfy(violation -> {
            Assertions.assertThat(violation.getMessage()).containsAnyOf(
                    "Email cannot be empty",
                    "Invalid email format"
            );
        });
    }

    // ----- PASSWORD TEST -------

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void password_ShouldHaveValidationError_WhenBlankGiven(String blankPassword) {
        // I. Arrange
        UserRequest request = createValidRequestBuilder()
                .password(blankPassword)
                .build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations).anySatisfy(v ->
                Assertions.assertThat(v.getMessage()).isEqualTo("Password cannot be empty")
        );
    }

    @Test
    public void password_ShouldHaveValidationError_WhenLengthIsTooShort() {
        // I. Arrange
        UserRequest request = createValidRequestBuilder()
                .password("12345") // 5 ký tự
                .build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Password must be at least 6 characters");
    }

    // ----- USERNAME TEST -------

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    public void userName_ShouldHaveValidationError_WhenBlankGiven(String blankName) {
        // I. Arrange
        UserRequest request = createValidRequestBuilder()
                .userName(blankName)
                .build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Name cannot be empty");
    }

    @Test
    public void userName_ShouldHaveValidationError_WhenLengthIsTooShort() {
        // I. Arrange
        UserRequest request = createValidRequestBuilder()
                .userName("abc") // Chỉ có 3 ký tự, yêu cầu tối thiểu 5
                .build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("UserName must be at least 5 characters");
    }

    // ----- ROLENAME TEST -------

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    public void roleName_ShouldHaveValidationError_WhenBlankGiven(String blankRole) {
        // I. Arrange
        UserRequest request = createValidRequestBuilder()
                .roleName(blankRole)
                .build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("roleName cannot be empty");
    }
    // ----- STATUS TEST -------

    @Test
    public void status_ShouldHaveValidationError_WhenStatusIsNull() {
        // I. Arrange
        UserRequest request = createValidRequestBuilder()
                .status(null)
                .build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Status cannot be empty");
    }

    // ----- SUCCESS CASE -------

    @Test
    public void userRequest_ShouldHaveNoErrors_WhenDataIsValid() {
        // I. Arrange
        UserRequest request = createValidRequestBuilder().build();

        // II. Act
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        // III. Assert
        Assertions.assertThat(violations).isEmpty();
    }
}

