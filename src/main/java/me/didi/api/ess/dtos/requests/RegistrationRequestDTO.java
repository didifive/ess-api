package me.didi.api.ess.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Student;
import me.didi.api.ess.entities.Subject;

import java.util.List;
import java.util.stream.Collectors;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.NOT_BE_NULL_EMPTY_OR_BLANK;
import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.NOT_BE_NULL_OR_EMPTY;

public record RegistrationRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String studentId,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String classId,
        @NotEmpty(message = NOT_BE_NULL_OR_EMPTY)
        List<String> subjectsId
) {
    public static synchronized Registration toEntity(RegistrationRequestDTO dto) {
        return new Registration(
                new Student(dto.studentId()),
                new Clazz(dto.classId()),
                dto.subjectsId().stream()
                        .map(Subject::new)
                        .collect(Collectors.toSet())
        );
    }
}
