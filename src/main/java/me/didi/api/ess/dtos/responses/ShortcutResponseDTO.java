package me.didi.api.ess.dtos.responses;

import me.didi.api.ess.entities.Shortcut;

public record ShortcutResponseDTO(
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
