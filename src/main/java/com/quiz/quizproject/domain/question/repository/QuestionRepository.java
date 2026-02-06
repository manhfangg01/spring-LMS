package com.quiz.quizproject.domain.question.repository;

import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long>, JpaSpecificationExecutor<QuestionEntity> {
    Integer countByQuestionGroupId(Long groupId);

    @Query("SELECT MAX(q.orderIndex) FROM QuestionEntity q WHERE q.questionGroup.id = :targetGroupId")
    Optional<Integer> getMaxOrderIndexByQuestionGroupId(Long targetGroupId);

    @Modifying
    @Query("UPDATE QuestionEntity q SET q.orderIndex = q.orderIndex - 1"+" WHERE q.questionGroup.id = :questionGroupId AND q.orderIndex > :removedIndex")
    void decreaseOrderIndexOnQuestionGroup(Long questionGroupId, Integer removedIndex);
}
