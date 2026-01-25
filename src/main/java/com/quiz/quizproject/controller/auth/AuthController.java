package com.quiz.quizproject.controller.auth;

import com.quiz.quizproject.repository.RoleRepository;
import com.quiz.quizproject.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @GetMapping("signup")
    public String signUp() {
        return "auth";
    }
}
