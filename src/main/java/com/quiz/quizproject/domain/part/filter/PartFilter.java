package com.quiz.quizproject.domain.part.filter;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.util.constant.ExamType;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartFilter {
    private String title;
    private ExamType examType;
    private Integer orderIndex;
    private Long examId;

    public Specification<PartEntity> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(title)) {
                predicates.add(cb.like(root.get("title"), "%" + title + "%"));
            }

            if (examType != null) {
                predicates.add(cb.equal(root.get("examType"), examType));
            }

            if (orderIndex != null) {
                predicates.add(cb.equal(root.get("orderIndex"), orderIndex));
            }

            if (examId != null) {
                predicates.add(cb.equal(root.get("exam").get("id"), examId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
