package me.didi.api.ess.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.enums.GradeType;

import java.math.BigDecimal;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.UUID_ID_EXAMPLE;

public record GradeResponseRegistrationDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String subjectId,
        @Enumerated(EnumType.STRING)
        GradeType gradeType,
        @DecimalMin(value = "0.00")
        @DecimalMin(value = "10.00")
        BigDecimal value
) {

    public static synchronized GradeResponseRegistrationDTO toDto(Grade entity) {
        return new GradeResponseRegistrationDTO(
                entity.getId().getSubject().getId(),
                entity.getGradeType(),
                entity.getValue()
        );
    }
}
