package me.didi.api.ess.dtos.responses;

import me.didi.api.ess.entities.Student;

public record StudentResponseDTO(String id,
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
