package com.quiz.quizproject.repository;

import com.quiz.quizproject.entity.RoleEntity;
import com.quiz.quizproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
