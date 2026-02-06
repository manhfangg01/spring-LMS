package com.quiz.quizproject.domain.exam.repository;

import com.quiz.quizproject.domain.exam.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long>, JpaSpecificationExecutor<ExamEntity> {
    boolean existsByTitle(String title);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String title, Long id);

    @Query("SELECT COUNT(q) FROM QuestionEntity q " +
            "WHERE q.questionGroup.part.exam.id = :examId")
    Integer countQuestionsByExamId(@Param("examId") Long examId);
}
