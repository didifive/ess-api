package me.didi.api.ess.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import me.didi.api.ess.entities.Subject;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.NOT_BE_NULL_EMPTY_OR_BLANK;

public record SubjectRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String icon,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String title,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String description
) {
    public static synchronized Subject toEntity(SubjectRequestDTO dto) {
        return new Subject(
                dto.icon,
                dto.description,
                dto.title
        );
    }
}
