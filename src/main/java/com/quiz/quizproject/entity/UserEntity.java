package com.quiz.quizproject.entity;

import com.quiz.quizproject.base.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import java.util.List;

@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {
    private String userName;
    private String email;
    private String password;
    private String status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles;
}
