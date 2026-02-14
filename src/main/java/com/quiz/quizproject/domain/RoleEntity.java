package com.quiz.quizproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.domain.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity extends BaseEntity {
    private String name;
    private String description;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<UserEntity> users;
}
