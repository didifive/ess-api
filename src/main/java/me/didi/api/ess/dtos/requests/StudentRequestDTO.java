package me.didi.api.ess.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import me.didi.api.ess.entities.Student;

public record StudentRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String givenName,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String familyName,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String guardian,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String photo
) {

    public static final String NOT_BE_NULL_EMPTY_OR_BLANK = "Not be null, empty or blank";

    public static synchronized Student toEntity(StudentRequestDTO dto) {
        return new Student(
                dto.givenName(),
                dto.familyName(),
                dto.guardian(),
                dto.photo()
        );
    }
}
