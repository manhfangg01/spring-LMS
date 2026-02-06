package com.quiz.quizproject.domain.part.repository;

import com.quiz.quizproject.domain.part.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<PartEntity, Long>, JpaSpecificationExecutor<PartEntity> {
    List<PartEntity> findAllByExamId(Long examId);
    Integer countByExamId(Long examId);
}
