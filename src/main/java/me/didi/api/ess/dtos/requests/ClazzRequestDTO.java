package me.didi.api.ess.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Course;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static me.didi.api.ess.utils.constants.GeneralConstantsUtils.DATE_PATTERN;
import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.*;

public record ClazzRequestDTO(
        @Schema(example = CLASS_NAME_EXAMPLE)
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String name,
        @Schema(example = UUID_ID_EXAMPLE)
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String courseId,
        @Schema(type = STRING, format = DATE, example = DATE_EXAMPLE)
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String initDate,
        @Schema(type = STRING, format = DATE, example = DATE_EXAMPLE)
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String recoveryDate,
        @Schema(type = STRING, format = DATE, example = DATE_EXAMPLE)
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String endDate
) {

    public static synchronized Clazz toEntity(ClazzRequestDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        return new Clazz(
                dto.name(),
                new Course(dto.courseId()),
                LocalDate.parse(dto.initDate(), formatter),
                LocalDate.parse(dto.recoveryDate(), formatter),
                LocalDate.parse(dto.endDate(), formatter)
        );
    }
}
