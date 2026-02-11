package com.quiz.quizproject.domain.questionGroup.mapper;

import com.quiz.quizproject.domain.question.mapper.QuestionMapper;
import com.quiz.quizproject.domain.questionGroup.QuestionGroupEntity;
import com.quiz.quizproject.domain.questionGroup.dto.request.QuestionGroupRequest;
import com.quiz.quizproject.domain.questionGroup.dto.response.DetailedQuestionGroupResponse;
import com.quiz.quizproject.domain.questionGroup.dto.response.QuestionGroupResponse;
import com.quiz.quizproject.domain.sharedOption.mapper.SharedOptionMapper;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses ={QuestionMapper.class, SharedOptionMapper.class})
public interface QuestionGroupMapper {

    @Mapping(target = "partId", source = "part.id")
    QuestionGroupResponse toResponse(QuestionGroupEntity entity);

    @AfterMapping
    default void linkSharedOptions(@MappingTarget QuestionGroupEntity questionGroupEntity) {
        if (questionGroupEntity.getSharedOptions() != null) {
        questionGroupEntity.getSharedOptions().forEach(sharedOption -> sharedOption.setGroup(questionGroupEntity));
        }
    }

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
