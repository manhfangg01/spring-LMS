package com.quiz.quizproject;

import com.quiz.quizproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuizApplication   {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);

    }
}
