package com.quiz.quizproject.domain.user.mapper;

import com.quiz.quizproject.domain.user.UserEntity;
import com.quiz.quizproject.domain.user.dto.request.UserRequest;
import com.quiz.quizproject.domain.user.dto.response.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "roleName", source = "role.name")
    UserResponse toResponse(UserEntity entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    UserEntity toEntity(UserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Thanks to this line, if
                                                                                             // a field is null, it will
                                                                                             // not set it to null but
                                                                                             // keep the old value
  

    @Mapping(target = "role", ignore = true) // manually handled in service
    @Mapping(target = "password", ignore = true) // manually handled in service
    @Mapping(target = "userName", ignore = true) // manually handled uniqueness
    void updateEntityFromRequest(UserRequest request, @MappingTarget UserEntity entity);

}
