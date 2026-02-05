package com.quiz.quizproject.domain.questionGroup.mapper;

import com.quiz.quizproject.domain.questionGroup.dto.response.DetailedQuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.entity.QuestionGroupEntity;
import com.quiz.quizproject.domain.questionGroup.dto.request.QuestionGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionGroupMapper {

    @Mapping(target = "partId", source = "part.id")
    QuestionGroupResponse toResponse(QuestionGroupEntity entity);

    @Mapping(target = "partId", source = "part.id")
    DetailedQuestionGroupResponse toDetailedResponse(QuestionGroupEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "part", ignore = true) // Handled in service
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    QuestionGroupEntity toEntity(QuestionGroupRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "part", ignore = true) // Handled in service
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(QuestionGroupRequest request, @MappingTarget QuestionGroupEntity entity);
}
