package me.didi.api.ess.dtos.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.didi.api.ess.dtos.responses.StudentResponseDTO;

import java.util.List;

public record Registration(StudentResponseDTO student,
                           @JsonProperty("class")
                           Clazz clazz,
                           String registrationDate,
                           List<Grade> grades
) {
}
