package com.quiz.quizproject.domain.question.mapper;

import com.quiz.quizproject.domain.question.dto.request.QuestionOptionRequest;
import com.quiz.quizproject.domain.question.dto.request.QuestionRequest;
import com.quiz.quizproject.domain.question.dto.response.QuestionOptionResponse;
import com.quiz.quizproject.domain.question.dto.response.DetailedQuestionResponse;
import com.quiz.quizproject.domain.question.entity.QuestionEntity;
import com.quiz.quizproject.domain.question.entity.QuestionOptionEntity;
import com.quiz.quizproject.domain.sharedOption.mapper.SharedOptionMapper;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {SharedOptionMapper.class})
public interface QuestionMapper {

    QuestionEntity toEntity(QuestionRequest request);

    @AfterMapping
    default void linkOptions(@MappingTarget QuestionEntity questionEntity) {
        if (questionEntity.getQuestionOptions() != null) {
            // Nhận cha cho các option mapping
            questionEntity.getQuestionOptions().forEach(option -> option.setQuestion(questionEntity));
        }
    }

    @Mapping(target = "question", ignore = true)
    QuestionOptionEntity toOptionEntity(QuestionOptionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  

    @Mapping(target = "questionGroup", ignore = true)
    @Mapping(target = "userAnswers", ignore = true)
    void updateEntityFromRequest(QuestionRequest request, @MappingTarget QuestionEntity entity);

    @Mapping(target = "groupId", source = "questionGroup.id")
//    @Mapping(target = "correctAnswers", expression = "java(mapJsonToList(entity.getCorrectAnswers()))")
    DetailedQuestionResponse toResponse(QuestionEntity entity);

    QuestionOptionResponse toOptionResponse(QuestionOptionEntity entity);


    // helper for mapping JSON correctAnswers
//    default List<String> mapJsonToList(String json) {
//        if (json == null || json.isEmpty()) {
//            return Collections.emptyList();
//        }
//        try {
//            return new ObjectMapper().readValue(json, new TypeReference<>() {});
//        } catch (Exception e) {
//            throw new AppException(e.getClass().toString(), null, "Mapping Error",
//                    "Failed to map JSON to List<String>: " + e.getMessage());
//        }
//    }
}
