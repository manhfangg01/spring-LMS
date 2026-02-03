package com.quiz.quizproject.domain.user.service.impl;

import com.quiz.quizproject.domain.RoleEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
import com.quiz.quizproject.domain.user.filter.UserFilter;
import com.quiz.quizproject.domain.user.mapper.UserMapper;
import com.quiz.quizproject.domain.user.repo.UserRepository;
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
    public UserResponse createUser(UserRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST, "Lỗi nhập liệu","Email đã tồn tại"); // Nên dùng Custom Exception
        }

        UserEntity user = userMapper.toEntity(req);
        String finalUserName;
        do {
            finalUserName = RandomHelper.generateUniqueUserName(user.getUserName());
        }while(userRepo.existsByUserName(finalUserName));
        user.setUserName(finalUserName);
        user.setPassword(passwordEncoder.encode(req.password()));
        RoleEntity role = roleRepo.findByName(req.roleName())
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.BAD_REQUEST, "Lỗi nhập liệu","Role không tồn tại"));
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
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Không tìm thấy","Người dùng không tồn tại"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest req) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.NOT_FOUND, "Không tìm thấy","Người dùng không tồn tại"));
        if (userRepo.existsByEmailAndIdNot(req.email(), id)) {
            throw new AppException("ApiException", HttpStatus.BAD_REQUEST,
                    "Lỗi nhập liệu", "Email đã được sử dụng bởi người dùng khác");
        }
        RoleEntity role = roleRepo.findByName(req.roleName())
                .orElseThrow(() -> new AppException("ApiException", HttpStatus.BAD_REQUEST, "Lỗi nhập liệu","Role không tồn tại"));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(req.password()));
        String finalUserName = req.userName();
        while(userRepo.existsByUserNameAndIdNot(finalUserName,id)) {
             finalUserName = RandomHelper.generateUniqueUserName(user.getUserName());
        }
        user.setUserName(finalUserName);
        userMapper.updateEntityFromRequest(req, user);
        return userMapper.toResponse(userRepo.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
