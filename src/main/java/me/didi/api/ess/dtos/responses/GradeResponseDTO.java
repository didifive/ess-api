package me.didi.api.ess.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.enums.GradeType;

import java.math.BigDecimal;

public record GradeResponseDTO(
        StudentResponseDTO student,
        @JsonProperty("class")
        ClazzResponseDTO clazz,
        SubjectResponseDTO subject,
        @Enumerated(EnumType.STRING)
        GradeType gradeType,
        @DecimalMin(value = "0.00")
        @DecimalMin(value = "10.00")
        BigDecimal value
) {

    public static synchronized GradeResponseDTO toDto(Grade entity) {
        return new GradeResponseDTO(
                StudentResponseDTO
                        .toDto(entity.getId().getRegistration().getId().getStudent()),
                ClazzResponseDTO
                        .toDto(entity.getId().getRegistration().getId().getClazz()),
                SubjectResponseDTO
                        .toDto(entity.getId().getSubject()),
                entity.getGradeType(),
                entity.getValue()
        );
    }
}
