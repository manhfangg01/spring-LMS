package com.quiz.quizproject.domain.exam.mapper;

import com.quiz.quizproject.domain.exam.ExamEntity;
import com.quiz.quizproject.domain.exam.dto.request.ExamRequest;
import com.quiz.quizproject.domain.exam.dto.response.DetailedExamResponse;
import com.quiz.quizproject.domain.exam.dto.response.ExamResponse;
import com.quiz.quizproject.domain.part.mapper.PartMapper;
import org.mapstruct.*;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PartMapper.class)
public interface ExamMapper {
    ExamEntity toEntity(ExamRequest examRequest);

    ExamResponse toResponse(ExamEntity entity);

    DetailedExamResponse toDetailedResponse(ExamEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  

    @Mapping(target = "testAttempts", ignore = true)
    @Mapping(target = "examStatus", ignore = true)
    void updateEntityFromRequest(ExamRequest request, @MappingTarget ExamEntity entity);
}
