package com.quiz.quizproject.domain.questionGroup.repository;

import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroupEntity, Long>, JpaSpecificationExecutor<QuestionGroupEntity> {
    Integer countByPartId(Long partId);

    @Query("SELECT MAX(q.orderIndex) FROM QuestionGroupEntity q WHERE q.part.id = :partId")
    Optional<Integer> getMaxOrderIndexByPartId(@Param("partId") Long partId);

    @Modifying
    @Query("UPDATE QuestionGroupEntity q SET q.orderIndex = q.orderIndex - 1"+" WHERE q.part.id = :partId AND q.orderIndex > :removedIndex")
    void decreaseOrderIndexOnPart(@Param("partId") Long partId, @Param("removedIndex") Integer removedIndex);
}
