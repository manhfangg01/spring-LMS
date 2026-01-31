package com.quiz.quizproject.repository;

import com.quiz.quizproject.domain.RefreshTokenEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUser(UserEntity user);
    Optional<RefreshTokenEntity> findByValue(String value);
    @Modifying
    void deleteByUser(UserEntity user);
}
