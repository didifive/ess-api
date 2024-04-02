package me.didi.api.ess.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import me.didi.api.ess.entities.Subject;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.UUID_ID_EXAMPLE;

public record SubjectResponseDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String id,
        String icon,
        String title,
        String description
) {

    public static synchronized SubjectResponseDTO toDto(Subject entity) {
        return new SubjectResponseDTO(
                entity.getId(),
                entity.getIcon(),
                entity.getTitle(),
                entity.getDescription()
        );
    }
}
