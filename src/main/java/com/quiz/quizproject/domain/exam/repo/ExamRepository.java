package com.quiz.quizproject.domain.exam.repo;

import com.quiz.quizproject.domain.exam.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long>, JpaSpecificationExecutor<ExamEntity> {
    boolean existsByTitle(String title);
}
