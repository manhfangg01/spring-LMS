package com.quiz.quizproject.domain.part.mapper;

import com.quiz.quizproject.domain.part.PartEntity;
import com.quiz.quizproject.domain.part.dto.request.PartRequest;
import com.quiz.quizproject.domain.part.dto.response.DetailedPartResponse;
import com.quiz.quizproject.domain.part.dto.response.PartResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartMapper {

    @Mapping(target = "examId", source = "exam.id")
    PartResponse toResponse(PartEntity entity);

    @Mapping(target = "examId", source = "exam.id")
    DetailedPartResponse toDetailedResponse(PartEntity entity);

    @Mapping(target = "exam", ignore = true) // Handled in Service
    @Mapping(target = "questionGroups", ignore = true)
    PartEntity toEntity(PartRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  
    @Mapping(target = "exam", ignore = true) // Handled in Service
    @Mapping(target = "questionGroups", ignore = true)

    void updateEntityFromRequest(PartRequest request, @MappingTarget PartEntity entity);
}
