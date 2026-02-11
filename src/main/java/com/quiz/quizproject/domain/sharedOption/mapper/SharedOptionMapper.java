package com.quiz.quizproject.domain.sharedOption.mapper;

import com.quiz.quizproject.domain.sharedOption.SharedOptionEntity;
import com.quiz.quizproject.domain.sharedOption.dto.response.SharedOptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SharedOptionMapper {
    SharedOptionResponse toResponse(SharedOptionEntity entity);

}
