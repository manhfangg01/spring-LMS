package com.quiz.quizproject.domain.user.service;

import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
import com.quiz.quizproject.domain.user.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse createUser(UserRequest req);
    Page<UserResponse> getAllUsers(Pageable pageable, UserFilter filter);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest req);
    void deleteUser(Long id);
}
