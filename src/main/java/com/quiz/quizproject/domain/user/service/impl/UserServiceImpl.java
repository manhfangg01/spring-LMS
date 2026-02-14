package com.quiz.quizproject.domain.user.service.impl;

import com.quiz.quizproject.domain.RoleEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
import com.quiz.quizproject.domain.user.filter.UserFilter;
import com.quiz.quizproject.domain.user.mapper.UserMapper;
import com.quiz.quizproject.domain.user.repository.UserRepository;
import com.quiz.quizproject.domain.user.service.UserService;
import com.quiz.quizproject.repository.RoleRepository;
import com.quiz.quizproject.util.exception.handler.AppException;
import com.quiz.quizproject.util.random.RandomHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepo.existsByEmail(request.email())) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Input error", "Email already exists"); // Should
                                                                                                                   // use
                                                                                                                   // Custom
                                                                                                                   // Exception
        }

        UserEntity user = userMapper.toEntity(request);
        String finalUserName;
        do {
            finalUserName = RandomHelper.generateUniqueUserName(user.getUserName());
        } while (userRepo.existsByUserName(finalUserName));
        user.setUserName(finalUserName);
        user.setPassword(passwordEncoder.encode(request.password()));
        RoleEntity role = roleRepo.findByName(request.roleName())
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.BAD_REQUEST, "Input error",
                        "Role not found"));
        user.setRole(role);

        return userMapper.toResponse(userRepo.save(user));
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable, UserFilter filter) {
        return userRepo.findAll(filter.toSpecification(), pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(
                        () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not found", "User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(
                        () -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Not found", "User not found"));
        if (userRepo.existsByEmailAndIdNot(request.email(), id)) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST,
                    "Input error", "Email is already used by another user");
        }
        RoleEntity role = roleRepo.findByName(request.roleName())
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.BAD_REQUEST, "Input error",
                        "Role not found"));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.password()));
        String finalUserName = request.userName();
        // fix: Nên cho phép người chỉnh sửa lại tên họ mong muốn chứ không nên thêm chuỗi ngẫu nhiên đằng sau
        if(userRepo.existsByUserNameAndIdNot(finalUserName, id)) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Input error", "Username is already existed. Consider trying another");
        }
        user.setUserName(finalUserName);
        userMapper.updateEntityFromRequest(request, user);
        return userMapper.toResponse(userRepo.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
