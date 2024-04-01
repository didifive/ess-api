package me.didi.api.ess.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.didi.api.ess.entities.Clazz;

import java.time.LocalDate;

import static me.didi.api.ess.utils.constants.ConstantsUtils.DATE_PATTERN;

public record ClazzResponseDTO(
        String id,
        String name,
        CourseResponseDTO course,
        @JsonFormat(pattern = DATE_PATTERN)
        LocalDate initDate,
        @JsonFormat(pattern = DATE_PATTERN)
        LocalDate recoveryDate,
        @JsonFormat(pattern = DATE_PATTERN)
        LocalDate endDate
) {

    public static synchronized ClazzResponseDTO toDto(Clazz entity) {
        return new ClazzResponseDTO(
                entity.getId(),
                entity.getName(),
                CourseResponseDTO.toDto(entity.getCourse()),
                entity.getInitDate(),
                entity.getRecoveryDate(),
                entity.getEndDate()
        );
    }
}
