package me.didi.api.ess.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import me.didi.api.ess.entities.Clazz;

import java.time.LocalDate;

import static me.didi.api.ess.utils.constants.GeneralConstantsUtils.DATE_PATTERN;
import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.*;

public record ClazzResponseDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String id,
        @Schema(example = CLASS_NAME_EXAMPLE)
        String name,
        CourseResponseDTO course,
        @Schema(example = DATE_EXAMPLE)
        @JsonFormat(pattern = DATE_PATTERN)
        LocalDate initDate,
        @Schema(example = DATE_EXAMPLE)
        @JsonFormat(pattern = DATE_PATTERN)
        LocalDate recoveryDate,
        @Schema(example = DATE_EXAMPLE)
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
