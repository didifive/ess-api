package me.didi.api.ess.controllers;

import jakarta.validation.Valid;
import me.didi.api.ess.controllers.docs.ClazzControllerDocs;
import me.didi.api.ess.dtos.requests.ClazzRequestDTO;
import me.didi.api.ess.dtos.responses.ClazzResponseDTO;
import me.didi.api.ess.services.ClazzService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static me.didi.api.ess.utils.VerifyError.verifyBodyError;

@RestController
@RequestMapping("api/v1/class")
public class ClazzController implements ClazzControllerDocs {

    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    @PostMapping
    public ResponseEntity<ClazzResponseDTO> create(
            @RequestBody @Valid ClazzRequestDTO dto,
            BindingResult bindingResult) {

        verifyBodyError(bindingResult);

        ClazzResponseDTO savedClass =
                ClazzResponseDTO.toDto(clazzService.save(ClazzRequestDTO.toEntity(dto)));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedClass.id()).toUri();

        return ResponseEntity.created(uri).body(savedClass);
    }

    @GetMapping
    public ResponseEntity<List<ClazzResponseDTO>> findAll() {
        return ResponseEntity.ok().body(clazzService.findAll().stream()
                .map(ClazzResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClazzResponseDTO> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(ClazzResponseDTO.toDto(
                clazzService.findById(id)
        ));
    }
}
