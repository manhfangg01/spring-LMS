package com.quiz.quizproject.domain.exam.mapper;

import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.domain.exam.dto.request.ExamRequest;
import com.quiz.quizproject.domain.exam.dto.response.ExamResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExamMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "testAttempts", ignore = true)
    @Mapping(target = "totalQuestions", ignore = true)
    ExamEntity toEntity(ExamRequest examRequest);

    ExamResponse toResponse(ExamEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "testAttempts", ignore = true)
    @Mapping(target = "totalQuestions", ignore = true)
    @Mapping(target = "code", ignore = true)// Xử lý trùng
    void updateEntityFromRequest(ExamRequest request, @MappingTarget ExamEntity entity);
}
