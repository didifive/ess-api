package me.didi.api.ess.dtos.responses;

import me.didi.api.ess.entities.Subject;

public record SubjectResponseDTO(
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
