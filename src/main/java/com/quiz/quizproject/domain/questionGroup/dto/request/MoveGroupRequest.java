package com.quiz.quizproject.domain.questionGroup.dto.request;

public record MoveGroupRequest(
        Long targetPartId,
        Integer position)
{}
