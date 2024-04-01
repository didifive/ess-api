package me.didi.api.ess.dtos.responses;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.enums.GradeType;

import java.math.BigDecimal;

public record GradeResponseInnerDTO(
        String subjectId,
        @Enumerated(EnumType.STRING)
        GradeType gradeType,
        BigDecimal value
) {

    public static synchronized GradeResponseInnerDTO toDto(Grade entity) {
        return new GradeResponseInnerDTO(
                entity.getId().getSubject().getId(),
                entity.getGradeType(),
                entity.getValue()
        );
    }
}
