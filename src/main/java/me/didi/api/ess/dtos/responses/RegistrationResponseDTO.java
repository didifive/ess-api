package me.didi.api.ess.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.enums.RegistrationStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.didi.api.ess.utils.constants.GeneralConstantsUtils.DATE_PATTERN;

public record RegistrationResponseDTO(
        StudentResponseDTO student,
        @JsonProperty("class")
        ClazzResponseDTO clazz,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<SubjectResponseDTO> subjects,
        @JsonFormat(pattern = DATE_PATTERN)
        LocalDate registrationDate,
        @Enumerated(EnumType.STRING)
        RegistrationStatus status,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<GradeResponseRegistrationDTO> grades
) {

    public static synchronized RegistrationResponseDTO toDto(Registration entity) {
        List<SubjectResponseDTO> subjects = new ArrayList<>();
        if (Objects.nonNull(entity.getSubjects()))
            subjects = entity.getSubjects().stream().map(SubjectResponseDTO::toDto).toList();

        List<GradeResponseRegistrationDTO> grades = new ArrayList<>();
        if (Objects.nonNull(entity.getGrades()))
            grades = entity.getGrades().stream().map(GradeResponseRegistrationDTO::toDto).toList();

        return new RegistrationResponseDTO(
                StudentResponseDTO.toDto(entity.getId().getStudent()),
                ClazzResponseDTO.toDto(entity.getId().getClazz()),
                subjects,
                entity.getRegistrationDate(),
                entity.status(),
                grades
        );
    }
}
