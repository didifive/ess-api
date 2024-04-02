package me.didi.api.ess.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import me.didi.api.ess.entities.Shortcut;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.UUID_ID_EXAMPLE;

public record ShortcutResponseDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String id,
        String icon,
        String title,
        String description,
        String link
) {

    public static synchronized ShortcutResponseDTO toDto(Shortcut entity) {
        return new ShortcutResponseDTO(
                entity.getId(),
                entity.getIcon(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getLink()
        );
    }
}
