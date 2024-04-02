package me.didi.api.ess.controllers;

import jakarta.validation.Valid;
import me.didi.api.ess.controllers.docs.RegistrationControllerDocs;
import me.didi.api.ess.dtos.requests.RegistrationRequestDTO;
import me.didi.api.ess.dtos.responses.RegistrationResponseDTO;
import me.didi.api.ess.services.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static me.didi.api.ess.utils.VerifyError.verifyBodyError;

@RestController
@RequestMapping("api/v1/registration")
public class RegistrationController implements RegistrationControllerDocs {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponseDTO> create(
            @RequestBody @Valid RegistrationRequestDTO dto,
            BindingResult bindingResult) {

        verifyBodyError(bindingResult);

        RegistrationResponseDTO savedRegistration =
                RegistrationResponseDTO.toDto(registrationService.save(RegistrationRequestDTO.toEntity(dto)));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/student/{studentId}/class/{classId}")
                .buildAndExpand(
                        savedRegistration.student().id(),
                        savedRegistration.clazz().id())
                .toUri();

        return ResponseEntity.created(uri).body(savedRegistration);
    }

    @GetMapping
    public ResponseEntity<List<RegistrationResponseDTO>> findAll() {
        return ResponseEntity.ok().body(registrationService.findAll().stream()
                .map(RegistrationResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/student/{studentId}/class/{classId}")
    public ResponseEntity<RegistrationResponseDTO> findById(
            @PathVariable("studentId") String studentId,
            @PathVariable("classId") String classId) {
        return ResponseEntity.ok().body(RegistrationResponseDTO.toDto(
                registrationService.findById(studentId, classId)
        ));
    }
}
