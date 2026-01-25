package com.quiz.quizproject.service.auth.impl;

import com.quiz.quizproject.entity.UserEntity;
import com.quiz.quizproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return      User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(r -> r.getName().replace("ROLE_", "")).toArray(String[]::new))
                .build();
    }
}
