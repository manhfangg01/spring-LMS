package com.quiz.quizproject.domain.sharedOption.mapper;

import com.quiz.quizproject.domain.sharedOption.dto.SharedOptionRequest;
import com.quiz.quizproject.domain.sharedOption.dto.SharedOptionResponse;
import com.quiz.quizproject.domain.sharedOption.entity.SharedOptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SharedOptionMapper {
    SharedOptionEntity toEntity(SharedOptionRequest request);
    SharedOptionResponse toResponse(SharedOptionEntity entity);
}
