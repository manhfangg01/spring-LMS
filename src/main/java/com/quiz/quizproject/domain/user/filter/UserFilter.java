package com.quiz.quizproject.domain.user.filter;

import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.user.UserEntity_;
import com.quiz.quizproject.util.constant.UserStatus;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserFilter {
    private Long id;
    private String userName;
    private String email;
    private UserStatus status;
    private String role;

    public Specification<UserEntity> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get(UserEntity_.ID), id));
            }

            if (StringUtils.hasText(userName)) {
                predicates.add(cb.like(root.get(UserEntity_.USER_NAME), "%" + userName + "%"));
            }

            if (StringUtils.hasText(email)) {
                predicates.add(cb.like(root.get(UserEntity_.EMAIL), "%" + email + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get(UserEntity_.STATUS), status));
            }

            if (StringUtils.hasText(role)) {
                predicates.add(cb.equal(root.get(UserEntity_.ROLE).get("name"), role));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
