package me.didi.api.ess.controllers;

import jakarta.validation.Valid;
import me.didi.api.ess.controllers.docs.SubjectControllerDocs;
import me.didi.api.ess.dtos.requests.SubjectRequestDTO;
import me.didi.api.ess.dtos.responses.SubjectResponseDTO;
import me.didi.api.ess.services.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static me.didi.api.ess.utils.VerifyError.verifyRequestBodyError;

@RestController
@RequestMapping("api/v1/subject")
public class SubjectController implements SubjectControllerDocs {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectResponseDTO> create(
            @RequestBody @Valid SubjectRequestDTO dto,
            BindingResult bindingResult) {

        verifyRequestBodyError(bindingResult);

        SubjectResponseDTO savedSubject =
                SubjectResponseDTO.toDto(subjectService.save(SubjectRequestDTO.toEntity(dto)));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedSubject.id()).toUri();

        return ResponseEntity.created(uri).body(savedSubject);
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> findAll() {
        return ResponseEntity.ok().body(subjectService.findAll().stream()
                .map(SubjectResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(SubjectResponseDTO.toDto(
                subjectService.findById(id)
        ));
    }
}
