package me.didi.api.ess.controllers;

import jakarta.validation.Valid;
import me.didi.api.ess.controllers.docs.GradeControllerDocs;
import me.didi.api.ess.dtos.requests.GradeRequestDTO;
import me.didi.api.ess.dtos.responses.GradeResponseDTO;
import me.didi.api.ess.services.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static me.didi.api.ess.utils.VerifyError.verifyRequestBodyError;

@RestController
@RequestMapping("api/v1/grade")
public class GradeController implements GradeControllerDocs {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<GradeResponseDTO> create(
            @RequestBody @Valid GradeRequestDTO dto,
            BindingResult bindingResult) {

        verifyRequestBodyError(bindingResult);

        GradeResponseDTO savedGrade =
                GradeResponseDTO.toDto(gradeService.save(GradeRequestDTO.toEntity(dto)));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/student/{studentId}/class/{classId}/subject/{subjectId}")
                .buildAndExpand(
                        savedGrade.student().id(),
                        savedGrade.clazz().id(),
                        savedGrade.subject().id())
                .toUri();

        return ResponseEntity.created(uri).body(savedGrade);
    }

    @GetMapping
    public ResponseEntity<List<GradeResponseDTO>> findAll() {
        return ResponseEntity.ok().body(gradeService.findAll().stream()
                .map(GradeResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/student/{studentId}/class/{classId}/subject/{subjectId}")
    public ResponseEntity<GradeResponseDTO> findById(
            @PathVariable("studentId") String studentId,
            @PathVariable("classId") String classId,
            @PathVariable("subjectId") String subjectId) {
        return ResponseEntity.ok().body(GradeResponseDTO.toDto(
                gradeService.findById(studentId, classId, subjectId)
        ));
    }
}
