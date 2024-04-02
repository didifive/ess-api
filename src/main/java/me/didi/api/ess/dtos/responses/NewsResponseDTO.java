package me.didi.api.ess.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import me.didi.api.ess.entities.News;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.UUID_ID_EXAMPLE;

public record NewsResponseDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String id,
        String icon,
        String title,
        String description
) {

    public static synchronized NewsResponseDTO toDto(News entity) {
        return new NewsResponseDTO(
                entity.getId(),
                entity.getIcon(),
                entity.getTitle(),
                entity.getDescription()
        );
    }
}
