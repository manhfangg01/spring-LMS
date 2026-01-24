package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {
    private String name;
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;
}
