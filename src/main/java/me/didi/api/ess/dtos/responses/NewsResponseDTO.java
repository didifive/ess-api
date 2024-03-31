package me.didi.api.ess.dtos.responses;

import me.didi.api.ess.entities.News;

public record NewsResponseDTO(String id,
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
