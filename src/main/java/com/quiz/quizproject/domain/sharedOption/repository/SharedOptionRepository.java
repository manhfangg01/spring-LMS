package com.quiz.quizproject.domain.sharedOption.repository;

import com.quiz.quizproject.domain.sharedOption.entity.SharedOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedOptionRepository extends JpaRepository<SharedOptionEntity, Long> {
    List<SharedOptionEntity> findByOptionGroup(String optionGroup);
}
