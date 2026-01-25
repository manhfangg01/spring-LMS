package com.quiz.quizproject.repository;

import com.quiz.quizproject.entity.RefreshTokenEntity;
import com.quiz.quizproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUser(UserEntity user);
    Optional<RefreshTokenEntity> findByValue(String value);
    void deleteByUser(UserEntity user);
}
