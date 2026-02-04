package com.quiz.quizproject.domain.questionGroup.filter;

import com.quiz.quizproject.domain.questionGroup.entity.QuestionGroupEntity;
import com.quiz.quizproject.util.constant.QuestionType;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionGroupFilter {
    private String instructions;
    private QuestionType type;
    private Long partId;

    public Specification<QuestionGroupEntity> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(instructions)) {
                predicates.add(cb.like(root.get("instructions"), "%" + instructions + "%"));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (partId != null) {
                predicates.add(cb.equal(root.get("part").get("id"), partId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
