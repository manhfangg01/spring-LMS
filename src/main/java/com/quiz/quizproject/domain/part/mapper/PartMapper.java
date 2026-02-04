package com.quiz.quizproject.domain.part.mapper;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.dto.request.PartRequest;
import com.quiz.quizproject.domain.part.dto.response.DetailedPartResponse;
import com.quiz.quizproject.domain.part.dto.response.PartResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartMapper {

    PartResponse toResponse(PartEntity entity);

    DetailedPartResponse toDetailedResponse(PartEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exam", ignore = true) // Handled in Service
    @Mapping(target = "questionGroups", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PartEntity toEntity(PartRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exam", ignore = true) // Handled in Service
    @Mapping(target = "questionGroups", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(PartRequest request, @MappingTarget PartEntity entity);
}
