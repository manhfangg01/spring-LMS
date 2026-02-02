package com.quiz.quizproject.domain.user.mapper;

import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "roleName", source = "role.name")
    UserResponse toResponse(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(UserRequest request);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)// Nhờ dòng này khi 1 trường bị null thì nó sẽ không set null mà nó sẽ set bằng giá trị cũ
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true) // tự validate role ở service
    @Mapping(target = "password", ignore = true) // tự validate role ở service
    @Mapping(target = "userName", ignore = true) // tự xử lý không trùng
    void updateEntityFromRequest(UserRequest request, @MappingTarget UserEntity entity);

}
