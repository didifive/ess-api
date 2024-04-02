package me.didi.api.ess.dtos.requests;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.didi.api.ess.entities.Course;
import me.didi.api.ess.enums.Frequency;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.NOT_BE_NULL;
import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.NOT_BE_NULL_EMPTY_OR_BLANK;

public record CourseRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String name,
        @NotNull(message = NOT_BE_NULL)
        @Enumerated(EnumType.STRING)
        Frequency frequency
) {



    public static synchronized Course toEntity(CourseRequestDTO dto) {
        return new Course(
                dto.name(),
                dto.frequency()
        );
    }
}
