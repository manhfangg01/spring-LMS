package com.quiz.quizproject.domain.questionGroup.repository;

import com.quiz.quizproject.domain.questionGroup.entity.QuestionGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroupEntity, Long>, JpaSpecificationExecutor<QuestionGroupEntity> {
    @Modifying
    @Query("UPDATE QuestionGroupEntity g SET g.orderIndex = g.orderIndex + 1 " +
            "WHERE g.part.id = :partId AND g.orderIndex >= :position")
    void incrementOrderIndex(Long partId, int position);

    @Modifying
    @Query("UPDATE QuestionGroupEntity g SET g.orderIndex = g.orderIndex - 1 " +
            "WHERE g.part.id = :partId AND g.orderIndex > :position")
    void decrementOrderIndex(Long partId, int position);
}
