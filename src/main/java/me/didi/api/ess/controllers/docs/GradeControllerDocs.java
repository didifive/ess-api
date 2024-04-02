package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.requests.GradeRequestDTO;
import me.didi.api.ess.dtos.responses.GradeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Grade Endpoints")
public interface GradeControllerDocs {

    ResponseEntity<GradeResponseDTO> create(
            GradeRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<List<GradeResponseDTO>> findAll();

    ResponseEntity<GradeResponseDTO> findById(
            @PathVariable("studentId") String studentId,
            @PathVariable("classId") String classId,
            @PathVariable("subjectId") String subjectId
    );

}
