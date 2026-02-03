package com.quiz.quizproject.domain.part.repo;

import com.quiz.quizproject.domain.part.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<PartEntity, Long>, JpaSpecificationExecutor<PartEntity> {
}
