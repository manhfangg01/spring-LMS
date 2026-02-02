package com.quiz.quizproject.domain.question.mapper;

import com.quiz.quizproject.domain.question.dto.request.QuestionOptionRequest;
import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.QuestionOptionResponse;
import com.quiz.quizproject.domain.question.dto.response.QuestionResponse;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import com.quiz.quizproject.domain.question.entity.QuestionOptionEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "material", ignore = true)
    @Mapping(target = "userAnswers", ignore = true)
    @Mapping(target = "questionOptions", source = "questionOptions")
    QuestionEntity toEntity(QuestionRequest request);

    @AfterMapping
    default void linkOptions(@MappingTarget QuestionEntity questionEntity) {
        if (questionEntity.getQuestionOptions() != null) {
            // Nhận cha cho các option mapping
            questionEntity.getQuestionOptions().forEach(option -> option.setQuestion(questionEntity));
        }
    }

    @Mapping(target = "materialId", source = "material.id")
    QuestionResponse toResponse(QuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "question", ignore = true)
    QuestionOptionEntity toOptionEntity(QuestionOptionRequest request);

    QuestionOptionResponse toOptionResponse(QuestionOptionEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "material", ignore = true)
    @Mapping(target = "userAnswers", ignore = true)
    @Mapping(target = "questionOptions", ignore = true)
    void updateEntityFromRequest(QuestionRequest request, @MappingTarget QuestionEntity entity);
}
