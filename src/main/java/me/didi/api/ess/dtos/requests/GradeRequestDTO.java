package me.didi.api.ess.dtos.requests;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.entities.Student;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.enums.GradeType;

import java.math.BigDecimal;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.NOT_BE_NULL;
import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.NOT_BE_NULL_EMPTY_OR_BLANK;

public record GradeRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String studentId,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String classId,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String subjectId,
        @NotNull(message = NOT_BE_NULL)
        @Enumerated(EnumType.STRING)
        GradeType gradeType,
        @NotNull(message = NOT_BE_NULL)
        @DecimalMin(value = "0.00", message = "Valor mínimo deve ser igual ou maior que 0")
        @DecimalMax(value = "10.00", message = "Valor máximo deve ser igual ou menor que 10")
        BigDecimal value
) {
    public static synchronized Grade toEntity(GradeRequestDTO dto) {
        return new Grade(
                new Student(dto.studentId()),
                new Clazz(dto.classId()),
                new Subject(dto.subjectId()),
                dto.gradeType(),
                dto.value()
        );
    }
}
