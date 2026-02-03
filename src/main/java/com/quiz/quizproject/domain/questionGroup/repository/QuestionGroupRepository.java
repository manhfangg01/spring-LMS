package com.quiz.quizproject.domain.questionGroup.repository;

import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionGroupRepository
        extends JpaRepository<QuestionGroupEntity, Long>, JpaSpecificationExecutor<QuestionGroupEntity> {
}
