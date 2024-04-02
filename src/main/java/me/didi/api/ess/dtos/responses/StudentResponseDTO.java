package me.didi.api.ess.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import me.didi.api.ess.entities.Student;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.UUID_ID_EXAMPLE;

public record StudentResponseDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String id,
        String name,
        String guardian,
        String photo
) {

    public static synchronized StudentResponseDTO toDto(Student entity) {
        return new StudentResponseDTO(
                entity.getId(),
                entity.getGivenName() + " " + entity.getFamilyName(),
                entity.getGuardian(),
                entity.getPhoto()
        );
    }
}
