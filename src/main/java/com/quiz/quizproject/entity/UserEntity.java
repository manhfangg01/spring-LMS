package com.quiz.quizproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quiz.quizproject.base.BaseEntity;
import com.quiz.quizproject.util.constant.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    private String userName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String avatarUrl;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TestAttemptEntity> testAttempts;

}
