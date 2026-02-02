package com.quiz.quizproject.util.random;


import java.util.Random;

public class RandomHelper {
    public static String generateUniqueUserName(String baseName) {
        String newName = baseName;
        Random random = new Random();
        int randomNumber = random.nextInt(1000, 9999);
        newName = baseName + randomNumber;
        return newName;
    }
}
