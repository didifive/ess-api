package me.didi.api.ess.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Course;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static me.didi.api.ess.utils.constants.ConstantsUtils.DATE_PATTERN;

public record ClazzRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String name,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String courseId,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        @JsonFormat(pattern = DATE_PATTERN)
        String initDate,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        @JsonFormat(pattern = DATE_PATTERN)
        String recoveryDate,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        @JsonFormat(pattern = DATE_PATTERN)
        String endDate
) {

    public static final String NOT_BE_NULL_EMPTY_OR_BLANK = "Not be null, empty or blank";

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
