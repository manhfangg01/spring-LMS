package com.quiz.quizproject.domain.exam.filter;

import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.util.constant.ExamType;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamFilter {
    private String title;
    private ExamType examType;

    public Specification<ExamEntity> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(title)) {
                predicates.add(cb.like(root.get("title"), "%" + title + "%"));
            }

            if (examType != null) {
                predicates.add(cb.equal(root.get("examType"), examType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
